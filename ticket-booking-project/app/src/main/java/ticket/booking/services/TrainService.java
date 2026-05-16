package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private Train train;
    private List<Train> trainList;

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException{
        loadTrains();
    }

    public TrainService(Train train1) throws IOException {
        this.train = train1;
        loadTrains();
    }

    public void loadTrains() throws IOException {
        File trains = new File(TRAINS_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String sourceStation, String destinationStation) {
        return trainList.stream().filter(train1 -> isValid(train1, sourceStation , destinationStation)).collect(Collectors.toList());
    }

    public void addTrain(Train newTrain) throws IOException {

        Optional<Train> existingTrain = trainList.stream().filter(train1 -> train1.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();

        if(existingTrain.isPresent()){
            updateTrain(newTrain);
        }
        else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }

    }

    public void updateTrain(Train newTrain) throws IOException {

        OptionalInt idx = IntStream.range(0, trainList.size()).filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();

        if(idx.isPresent()){
            trainList.set(idx.getAsInt(), newTrain);
            saveTrainListToFile();
        }
        else{
            addTrain(newTrain);
        }

    }

    public boolean isValid(Train train1, String sourceStation, String destinationStation) {
        List<String> trains = train.getStations();

        int srcIdx = trains.indexOf(sourceStation.toLowerCase());
        int destIdx = trains.indexOf(destinationStation.toLowerCase());

        return srcIdx != -1 && destIdx != -1 && srcIdx < destIdx;
    }

    private void saveTrainListToFile() throws IOException {
        File trainFile = new File(TRAINS_PATH);
        objectMapper.writeValue(trainFile, trainList);
    }



}
