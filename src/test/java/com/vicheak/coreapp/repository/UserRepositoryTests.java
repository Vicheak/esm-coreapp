package com.vicheak.coreapp.repository;

import com.vicheak.coreapp.api.user.User;
import com.vicheak.coreapp.api.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup(){
        User user = User.builder()
                .uuid("2a2c6665-692d-40d7-ab4e-4daa20602460")
                .username("suonvicheak")
                .email("suonvicheak991@gmail.com")
                .password("$2a$12$Djtu3PHeGr9aeoYbmQP3AOhw4GDMCeTwypivlBVNqYb0EMMLMIqIWabc")
                .nickname("ADMIN")
                .isVerified(false)
                .isDeleted(false)
                .build();

        userRepository.save(user);
    }

    @Test
    public void testFindByUuid(){
        //given

        //when
        Optional<User> userOptional =
                userRepository.findByUuid("2a2c6665-692d-40d7-ab4e-4daa20602460");
        User user = userOptional.orElse(null);

        //then
        Assertions.assertNotNull(user);
    }

    @Test
    public void testFindByUuidThrow(){
        //given

        //when
        Optional<User> userOptional =
                userRepository.findByUuid("2a2c6665-692d-40d7-ab4e-4daa20602460f");
        User user = userOptional.orElse(null);

        //then
        Assertions.assertNull(user);
    }

    @Test
    public void testExistsByUsernameIgnoreCase(){
        //given

        //when
        Boolean isExisted = userRepository.existsByUsernameIgnoreCase("suonvicheak");

        //then
        Assertions.assertTrue(isExisted);
    }

    @Test
    public void testExistsByEmailIgnoreCase(){
        //given

        //when
        Boolean isExisted = userRepository.existsByEmailIgnoreCase("suonvicheak991@gmail.com");

        //then
        Assertions.assertTrue(isExisted);
    }

    @Test
    public void testCheckUserByUuid(){
        //given

        //when
        Boolean isExisted = userRepository.checkUserByUuid("12345678");

        //then
        Assertions.assertFalse(isExisted);
    }

    @Test
    public void testUpdateIsDeletedStatusByUuid(){
        //given
        User user = User.builder()
                .uuid("2a2c6665-692d-40d7-ab4e-4daa20602460")
                .username("suonvicheak")
                .email("suonvicheak991@gmail.com")
                .password("$2a$12$Djtu3PHeGr9aeoYbmQP3AOhw4GDMCeTwypivlBVNqYb0EMMLMIqIWabc")
                .nickname("ADMIN")
                .isVerified(false)
                .isDeleted(true)
                .build();

        //when
        userRepository.updateIsDeletedStatusByUuid("2a2c6665-692d-40d7-ab4e-4daa20602460", true);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getIsDeleted(), true);
    }

}
