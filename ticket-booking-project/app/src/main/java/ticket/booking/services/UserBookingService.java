package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public UserBookingService(User user1) throws IOException
    {
        this.user = user1;
//        File users = new File(USERS_PATH);

        //"Read this JSON as a List containing User objects. (Tells jackson what type it needs to convert to...)"
//        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});

        loadUsers();
    }

    public void loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword() , user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return true;
        }catch (IOException ex){
            return false;
        }
    }

    private void saveUserListToFile() throws IOException {
        File userFiles = new File(USERS_PATH);
        objectMapper.writeValue(userFiles , userList);
    }
//    json --> Object(User) = deserialize
//    Object(User) --> json = serialize

    public void fetchBooking(){
        user.printTickets();
    }

    public boolean cancelBooking(String ticketId){
        boolean remove = user.getTicketBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        if(remove){
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return true;
        }
        else{
            System.out.println("Ticket with ID " + ticketId + " has been cancelled.");
            return false;
        }
    }

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        }
        catch (IOException ex){
            return new ArrayList<>();
        }
    }



}
