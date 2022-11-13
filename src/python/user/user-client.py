import json

import requests


class KeysUtil:
    def __init__(self):
        self.keys = None
        self.keys_api_path = "http://localhost:8080/api/v1/keys"

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
        response = requests.get(self.keys_api_path + "/generate")
        return response.json()

    def sign(self, message):
        response = requests.get(self.keys_api_path + "/sign",
                                params={"private_key": self.keys["private_key"], "message": message})
        return response.text


class UserClient:
    def __init__(self):
        self.keys_util = KeysUtil()
        self.keys = self.keys_util.init_user_keys()
        self.transactions = []
        self.transactions_api = "http://localhost:8080/api/v1/transaction"
        self.node_api = "http://localhost:8080/api/v1/node"

    def sign_transaction(self, message):
        return self.keys_util.sign(message)

    def send_transactions(self):
        transaction = {
            "sender_address": self.keys["public_key"],
            "transaction_info": self.transactions
        }
        request = requests.post(self.transactions_api + "/add", json=transaction)
        if request.status_code == 200:
            self.transactions = []
            print("Transactions sent")
        else:
            print("Error while sending transactions")
            print(request.status_code)
            print("Transactions not sent")

    def get_balance(self):
        response = requests.get(self.node_api + "/balance", params={"address": self.keys["public_key"]})
        return response.text

    def add_transaction(self, recipient, coins_amount):
        nonce = self.get_nonce()
        message = "{}{}{}{}".format(nonce, self.keys["public_key"], coins_amount, recipient)
        transaction_output = {
            "sender_address": self.keys["public_key"],
            "amount": coins_amount,
            "receiver_address": recipient,
            "signature": self.sign_transaction(message),
            "nonce": nonce,
        }
        self.transactions.append(transaction_output)

    def get_nonce(self):
        response = requests.get(self.transactions_api + "/nonce")
        return response.text


if __name__ == "__main__":
    user = UserClient()
    running = True
    while running:
        print("1. Create transaction")
        print("2. Send created transactions")
        print("3. Get balance before sending transactions")
        print("4. Exit")
        choice = input("Your choice: ")
        if choice == "1":
            recipients = input("Receiver: ")
            amount = input("Input amount of coins: ")
            user.add_transaction(recipients, amount)
        elif choice == "2":
            user.send_transactions()
        elif choice == "3":
            print("User balance: {}".format(user.get_balance()))
        elif choice == "4":
            running = False
        else:
            print("Invalid choice")
