package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user1) throws IOException
    {
        this.user = user1;
        File users = new File(USERS_PATH);

        //"Read this JSON as a List containing User objects. (Tells jackson what type it needs to convert to...)"
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword() , user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

//  Start at 3:03:46

}
