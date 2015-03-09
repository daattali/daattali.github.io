import java.util.Stack;

public class Debug
{

    private static boolean debugOn = false;
    private static Stack oldStates = new Stack();

    public static boolean isOn()
    {
        return debugOn;
    }

    public static boolean isOff()
    {
        return isOn() ^ true;
    }

    public static void turnOn()
    {
        oldStates.push(new Boolean(debugOn));
        debugOn = true;
    }

    public static void turnOff()
    {
        oldStates.push(new Boolean(debugOn));
        debugOn = false;
    }

    public static void restoreState()
    {
        if(oldStates.empty())
        {
            debugOn = false;
        } else
        {
            debugOn = ((Boolean)oldStates.pop()).booleanValue();
        }
    }

    public static void print(String message)
    {
        if(debugOn)
        {
            System.out.print(message);
        }
    }

    public static void println(String message)
    {
        if(debugOn)
        {
            System.out.println(message);
        }
    }

    public Debug()
    {
    }

}
