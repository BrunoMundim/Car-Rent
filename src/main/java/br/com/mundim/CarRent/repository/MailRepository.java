package br.com.mundim.CarRent.repository;

import br.com.mundim.CarRent.model.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
