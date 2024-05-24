package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.slip.PaymentState;
import com.vicheak.coreapp.api.slip.PaymentStateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PaymentStateRepositoryTests {

    @Autowired
    private PaymentStateRepository paymentStateRepository;

    @Test
    public void testFindByStatusIgnoreCase(){
        //given

        //when
        Optional<PaymentState> paymentStateOptional = paymentStateRepository.findByStatusIgnoreCase("paid");
        PaymentState paymentState = paymentStateOptional.orElse(null);

        //then
        Assertions.assertNotNull(paymentState);
    }

    @Test
    public void testFindByStatusIgnoreCaseThrow(){
        //given

        //when
        Optional<PaymentState> paymentStateOptional = paymentStateRepository.findByStatusIgnoreCase("paids");
        PaymentState paymentState = paymentStateOptional.orElse(null);

        //then
        Assertions.assertNull(paymentState);
    }

    @Test
    public void testExistsByStatusIgnoreCase(){
        //given

        //when
        Boolean isExisted = paymentStateRepository.existsByStatusIgnoreCase("paid");

        //then
        Assertions.assertTrue(isExisted);
    }

}
