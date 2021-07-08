import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class BlockParseException extends Exception {
    BlockParseException(String msg) {
        super(msg);
    }
}


class Block {

    int id;
    long timestamp;
    long magic;
    String hashprev;
    String hash;

    static Block parseBlock(String strBlock) throws BlockParseException {
        if (strBlock.length() == 0) {
            return null;
        }

        if (!(strBlock.contains("Block:")
            && strBlock.contains("Timestamp:"))) {

            return null;
        }

        Block block = new Block();

        List<String> lines = strBlock
            .lines()
            .map(String::strip)
            .filter(e -> e.length() > 0)
            .collect(Collectors.toList());

        if (lines.size() != 9) {
            throw new BlockParseException("Every block should " +
                "contain 9 lines of data");
        }

        if (!lines.get(0).equals("Block:")) {
            throw new BlockParseException("First line of every block " +
                "should be \"Block:\"");
        }

        if (!lines.get(1).startsWith("Id:")) {
            throw new BlockParseException("Second line of every block " +
                "should start with \"Id:\"");
        }

        String id = lines.get(1).split(":")[1]
            .strip().replace("-", "");
        boolean isNumeric = id.chars().allMatch(Character::isDigit);

        if (!isNumeric) {
            throw new BlockParseException("Id should be a number");
        }

        block.id = Integer.parseInt(id);



        if (!lines.get(2).startsWith("Timestamp:")) {
            throw new BlockParseException("Third line of every block " +
                "should start with \"Timestamp:\"");
        }

        String timestamp = lines.get(2).split(":")[1]
            .strip().replace("-", "");
        isNumeric = timestamp.chars().allMatch(Character::isDigit);

        if (!isNumeric) {
            throw new BlockParseException("Timestamp should be a number");
        }

        block.timestamp = Long.parseLong(timestamp);


        if (!lines.get(3).startsWith("Magic number:")) {
            throw new BlockParseException("4-th line of every block " +
                "should start with \"Magic number:\"");
        }

        String magic = lines.get(3).split(":")[1]
            .strip().replace("-", "");
        isNumeric = magic.chars().allMatch(Character::isDigit);

        if (!isNumeric) {
            throw new BlockParseException("Timestamp should be a number");
        }

        block.magic = Long.parseLong(magic);



        if (!lines.get(4).equals("Hash of the previous block:")) {
            throw new BlockParseException("5-th line of every block " +
                "should be \"Hash of the previous block:\"");
        }

        if (!lines.get(6).equals("Hash of the block:")) {
            throw new BlockParseException("7-th line of every block " +
                "should be \"Hash of the block:\"");
        }

        String prevhash = lines.get(5).strip();
        String hash = lines.get(7).strip();

        if (!(prevhash.length() == 64 || prevhash.equals("0"))
            || !(hash.length() == 64)) {

            throw new BlockParseException("Hash length should " +
                "be equal to 64 except \"0\"");
        }

        block.hash = hash;
        block.hashprev = prevhash;

        return block;
    }


    static List<Block> parseBlocks(String output) throws BlockParseException {
        String[] strBlocks = output.substring(
            output.indexOf("Block:")).split("\n\n");

        List<Block> blocks = new ArrayList<>();

        for (String strBlock : strBlocks) {
            Block block = parseBlock(strBlock.strip());
            if (block != null) {
                blocks.add(block);
            }
        }

        return blocks;
    }
}

class Clue {
    String zeros;
    Clue(int n) {
        zeros = "0".repeat(n);
    }
}


public class BlockchainTest extends StageTest<Clue> {

    List<String> previousOutputs = new ArrayList<>();

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            new TestCase<Clue>().setInput("0").setAttach(new Clue(0)),
            new TestCase<Clue>().setInput("1").setAttach(new Clue(1)),
            new TestCase<Clue>().setInput("2").setAttach(new Clue(2)),
            new TestCase<Clue>().setInput("0").setAttach(new Clue(0)),
            new TestCase<Clue>().setInput("1").setAttach(new Clue(1)),
            new TestCase<Clue>().setInput("2").setAttach(new Clue(2))
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {

        if (previousOutputs.contains(reply)) {
            return new CheckResult(false,
                "You already printed this text in the previous tests");
        }

        previousOutputs.add(reply);

        List<Block> blocks;
        try {
            blocks = Block.parseBlocks(reply);
        } catch (BlockParseException ex) {
            return new CheckResult(false, ex.getMessage());
        } catch (Exception ex) {
            return CheckResult.wrong("");
        }

        if (blocks.size() != 5) {
            return new CheckResult(false,
                "You should output 5 blocks, found " + blocks.size());
        }

        for (int i = 1; i < blocks.size(); i++) {
            Block curr = blocks.get(i - 1);
            Block next = blocks.get(i);

            if (curr.id + 1 != next.id) {
                return new CheckResult(false,
                    "Id`s of blocks should increase by 1");
            }

            if (next.timestamp < curr.timestamp) {
                return new CheckResult(false,
                    "Timestamp`s of blocks should increase");
            }

            if (!next.hashprev.equals(curr.hash)) {
                return new CheckResult(false, "Two hashes aren't equal, " +
                    "but should");
            }

            if (!next.hash.startsWith(clue.zeros)) {
                return new CheckResult(false,
                    "Hash should start with some zeros");
            }
        }


        return CheckResult.correct();
    }
}
