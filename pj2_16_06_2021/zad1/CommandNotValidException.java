public class CommandNotValidException extends Exception{

    public CommandNotValidException(){
        super();
    }

    public CommandNotValidException(String messageForTheRedHood){
        super(messageForTheRedHood);
    }
}