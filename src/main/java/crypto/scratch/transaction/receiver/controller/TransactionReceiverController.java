package crypto.scratch.transaction.receiver.controller;

import crypto.scratch.block.components.transaction.Transaction;
import crypto.scratch.transaction.receiver.service.TransactionReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction/")
@RequiredArgsConstructor
@Validated
public class TransactionReceiverController {
	private final TransactionReceiverService service;

	@PostMapping("/add")
	public HttpStatus addTransaction(@RequestBody @Valid Transaction transaction) {
		boolean isAdded = service.addTransaction(transaction);
		return isAdded ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
	}

	@GetMapping("/nonce")
	public String getSecurityIndex() {
		return service.getRandomIndex();
	}
}
