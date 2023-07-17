package br.com.mundim.CarRent.service;

import br.com.mundim.CarRent.model.dto.RentDTO;
import br.com.mundim.CarRent.model.dto.mail.SucessfullRentMailDTO;
import br.com.mundim.CarRent.model.dto.mail.SucessfullReturnMailDTO;
import br.com.mundim.CarRent.model.entity.Car;
import br.com.mundim.CarRent.model.entity.Rent;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.utils.DateToString;
import br.com.mundim.CarRent.utils.NumberToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RentMailService {

    private final MailService mailService;
    private final RentService rentService;
    private final CarService carService;
    private final UserService userService;

    @Autowired
    public RentMailService(MailService mailService, RentService rentService, CarService carService, UserService userService) {
        this.mailService = mailService;
        this.rentService = rentService;
        this.carService = carService;
        this.userService = userService;
    }

    public void sucessfullRentMail(Rent rent) {
        User user = userService.findById(rent.getUserId());
        Car car = carService.findById(rent.getCarId());
        SucessfullRentMailDTO sucessfullRentMailDTO = new SucessfullRentMailDTO(
                user.getName(),
                car.getBrand(),
                car.getModel(),
                DateToString.localDateTimeToString(rent.getRentDay()),
                DateToString.localDateTimeToString(rent.getReturnDay()),
                NumberToString.doubleToString(rent.getTotalValue(), 2)
        );
        mailService.sendEmailWithTemplate("no-reply: Success Rent", "SuccessfullRent", sucessfullRentMailDTO);
    }

    public void sucessfullReturnMail(Rent rent) {
        User user = userService.findById(rent.getUserId());
        Car car = carService.findById(rent.getCarId());
        SucessfullReturnMailDTO sucessfullReturnMailDTO = new SucessfullReturnMailDTO(
                user.getName(),
                car.getBrand(),
                car.getModel(),
                DateToString.localDateTimeToString(rent.getRentDay()),
                DateToString.localDateTimeToString(rent.getReturnDay()),
                NumberToString.doubleToString(rent.getTotalValue(), 2),
                rent.getReturnStatus().toString()
        );
        mailService.sendEmailWithTemplate("no-reply: Success Return", "SuccessfullReturn", sucessfullReturnMailDTO);
    }
}
