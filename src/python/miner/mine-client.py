import sys
import time
import json
import requests
import hashlib
import random
import multiprocessing as mp


def generate_random_number():
    return random.randint(-2 ** 31, 2 ** 31 - 1)


def hash_string(string):
    return hashlib.sha256(string.encode()).hexdigest()


class KeysUtil:
    def __init__(self):
        self.keys = None
        self.identifier_api_path = "http://localhost:8080/api/v1/keys"

    def init_user_keys(self):
        self.get_keys_from_file()
        if self.keys is None:
            self.keys = self.generate_keys_pair()
            self.save_keys_to_file()
        return self.keys

    def get_keys_from_file(self):
        try:
            with open("keys.json", "r") as file:
                self.keys = json.load(file)
                file.close()
        except FileNotFoundError:
            return None

    def save_keys_to_file(self):
        with open("keys.json", "w") as file:
            file.write(json.dumps(self.keys))

    def generate_keys_pair(self):
        response = requests.get(self.identifier_api_path + "/generate")
        return response.json()


class MineClient:
    def __init__(self):
        self.node_api_path = "http://localhost:8080/api/v1/node"
        self.identifier_api_path = "http://localhost:8080/api/v1/keys"
        block_scratch_pair = self.get_block_scratch()
        self.scratch = block_scratch_pair["scratch"]
        self.scratch_str = block_scratch_pair["to_string"]
        self.header = self.scratch["header"]
        self.keys = KeysUtil().init_user_keys()
        reward_message = "{}{}".format(self.keys["public_key"], self.scratch["reward"])
        self.reward_obj = {
            "miner_address": self.keys["public_key"],
            "reward": self.scratch["reward"],
            "signature": self.sign(reward_message)
        }
        self.reward_obj_str = "{}{}".format(reward_message, self.reward_obj["signature"])

    def sign(self, reward_message):
        response = requests.get(self.identifier_api_path + "/sign",
                                params={"private_key": self.keys["private_key"], "message": reward_message})
        return response.text

    def get_block_scratch(self):
        response = requests.get(self.node_api_path + "/scratch")
        return response.json()

    def build_block(self, result):
        return {
            "header": {
                "parent_hash": self.header["parent_hash"],
                "hash": result[0],
                "timestamp": self.header["timestamp"],
                "difficulty": self.header["difficulty"],
                "height": self.header["height"]
            },
            "transactions": self.scratch["transactions"],
            "reward_info": self.reward_obj,
            "nonce": result[1]
        }

    def start_miners(self, number_of_miners):
        number_of_miners = min(number_of_miners, mp.cpu_count())
        pool = mp.Pool(number_of_miners)
        for i in range(number_of_miners):
            pool.apply_async(self.start)
        self.start()
        while True:
            time.sleep(3)
            response = requests.get(self.node_api_path + "/valid/last",
                                    params={"hash": self.header["parent_hash"]})
            if response.text == "false":
                print("Scratch is no longer the same, updating...")
                pool.terminate()
                self.update_scratch()
                pool = mp.Pool(number_of_miners)
                for i in range(number_of_miners):
                    pool.apply_async(self.start)

    def update_scratch(self):
        block_scratch_pair = self.get_block_scratch()
        self.scratch = block_scratch_pair["scratch"]
        self.header = self.scratch["header"]
        self.scratch_str = block_scratch_pair["to_string"]

    def start(self):
        str_block_with_reward = "{}{}".format(self.scratch_str, self.reward_obj_str)
        result = self.mine_block(str_block_with_reward)
        mined_block = self.build_block(result)
        self.send_new_block(mined_block)

    def send_new_block(self, block):
        response = requests.post(self.node_api_path + "/block", json=block)
        if response.status_code == 200:
            print("Block added successfully with {}".format(block["header"]["hash"]))

    def mine_block(self, str_to_hash):
        while True:
            nonce = generate_random_number()
            sha_hash = hash_string("{}{}".format(str_to_hash, nonce))
            if sha_hash.startswith("0" * self.header["difficulty"]):
                return sha_hash, nonce


if __name__ == '__main__':
    MineClient().start_miners(int(sys.argv[1]))
