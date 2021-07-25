package ibm.java.academy.cerfiticationsapp.service;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import ibm.java.academy.cerfiticationsapp.model.ConfirmationToken;
import ibm.java.academy.cerfiticationsapp.repository.ConfirmationTokenJpaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

	private final ConfirmationTokenJpaRepository confirmationTokenRepository;

	public void saveConfirmationToken(ConfirmationToken confirmationToken) {
		confirmationTokenRepository.save(confirmationToken);
	}

	public void deleteConfirmationToken(Long id) {
		confirmationTokenRepository.deleteById(id);
	}

	public ConfirmationToken loginViaToken(String token) {
		Optional<ConfirmationToken> find = findConfirmationTokenByToken(token);

		if (find.isPresent()) {
			return find.get();
		}
		else {
            throw new BadCredentialsException(MessageFormat.format("Token not found", token));
        }
	}

	public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {
		return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
	}

}