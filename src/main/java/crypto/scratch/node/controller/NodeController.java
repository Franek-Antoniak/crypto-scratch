package crypto.scratch.node.controller;

import crypto.scratch.block.Block;
import crypto.scratch.block.scratch.BlockScratch;
import crypto.scratch.node.service.NodeService;
import crypto.scratch.node.structure.BlockScratchPair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/node/")
@RequiredArgsConstructor
@Validated
public class NodeController {
	private final NodeService service;

	@PostMapping("/block")
	public ResponseEntity<Void> addBlock(@RequestBody @Valid Block block) {
		boolean isAdded = service.addBlock(block);
		return new ResponseEntity<>(isAdded ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/scratch")
	public BlockScratchPair getNewBlockScratch() {
		BlockScratch scratch = service.getBlockScratch();
		return new BlockScratchPair(scratch, scratch.hashArguments());
	}

	@GetMapping("/valid/last")
	public boolean checkLastHash(String hash) {
		return service.isLastHashEquals(hash);
	}

	@GetMapping("/balance")
	public BigDecimal getBalance(String address) {
		return service.getBalance(address);
	}
}
