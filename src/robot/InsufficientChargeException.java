package robot;

public class InsufficientChargeException extends Exception
{
    public InsufficientChargeException()
    {
        super("Charge de la batterie insuffisante");
    }
}
