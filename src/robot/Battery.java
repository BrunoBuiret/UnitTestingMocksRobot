package robot;

import java.util.Timer;
import java.util.TimerTask;

public class Battery
{
    /**
     * Niveau de charge de la batterie
     */
    private float chargeLevel;
    private final long CHARGE_TOP = 1000;

    public Battery()
    {
        chargeLevel = 100;
    }

    public void charge()
    {
        chargeLevel = chargeFunction(chargeLevel);
    }

    private static float chargeFunction(float charge)
    {
        return charge * 1.1f + 1;
    }

    /**
     * Au landing du robot, la batterie se recharge automatiquement toutes les
     * CHARGE_TOP ms.
     */
    public void setUp()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            //definition de la tache a accomplir
            @Override
            public void run()
            {
                charge();
            }
        }, 0, CHARGE_TOP);
    }

    public float getChargeLevel()
    {
        return chargeLevel;
    }
    
    public long getChargeTop()
    {
        return this.CHARGE_TOP;
    }

    /**
     * Utilisation de la batterie pour realiser une tache necessitant de
     * decharger la batterie
     *
     * @param energy valeur prevue de decharge de la batterie
     * @throws InsufficientChargeException si la batterie n'est pas assez
     * chargee pour
     */
    public void use(double energy) throws InsufficientChargeException
    {
        if(chargeLevel < energy)
        {
            throw new InsufficientChargeException();
        }
        // chargeLevel = (float) energy;
        chargeLevel -= (float) energy;
    }

    /**
     *
     * @param neededEnergy
     * @return temps (millisecondes) necessaire pour que la batterie ait une
     * charge egale a neededEnergy
     */
    public long timeToSufficientCharge(double neededEnergy)
    {
        int clock = 0;
        float charge = chargeLevel;
        while(charge < neededEnergy)
        {
            charge = chargeFunction(charge);
            clock++;
        }
        return clock * CHARGE_TOP;
    }

}
