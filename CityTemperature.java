import org.perf4j.StopWatch;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Intellij IDEA.
 * User: Sardor Navruzov
 * Date: 5/13/13
 * Time: 1:10 PM
 */
public class CityTemperature
{

    private HashMap<String,Integer> redu= new HashMap<>();
    private HashMap<String,List<Integer>> mapOut = new HashMap<>();
    private Long lgOut;


    public Long getLgOut()
    {
        return lgOut;
    }

    public void setLgOut(Long lgOut)
    {
        this.lgOut = lgOut;
    }

    public CityTemperature() throws InterruptedException
    {
        List<Map<String, String>> states = new ArrayList<>();
        Map mp=new LinkedHashMap<String,String[]>();

        mp.put("City11", "MP,77");
        mp.put("City12", "MP,72");
        mp.put("City13", "MP,76");
        states.clear();
        states.add(mp);
        Thread thread = new StartMapThread(shuffleOut(map(states)));
        System.out.println(thread.getName());
        thread.run();
        mp.clear();

        mp.put("City31", "OR,69");
        mp.put("City32", "OR,71");
        mp.put("City33", "OR,76");
        states.clear();
        states.add(mp);
        thread = new StartMapThread(shuffleOut(map(states)));
        System.out.println(thread.getName());
        thread.run();
        mp.clear();

        mp.put("City21", "CG,70");
        mp.put("City22", "CG,72");
        mp.put("City23", "CG,75");
        states.clear();
        states.add(mp);
        thread = new StartMapThread(shuffleOut(map(states)));
        System.out.println(thread.getName());
        thread.run();
        mp.clear();

        mp.put("City31", "QT,69");
        mp.put("City32", "QT,71");
        mp.put("City33", "QT,76");
        states.clear();
        states.add(mp);
        new StartMapThread(shuffleOut(map(states))).run();
        
    }

    public HashMap<String,Integer> getReduceDone()
    {
        return redu;
    }

    private List<String> map(List<Map<String,String>> mapIn)
    {
        List<String> list = new ArrayList<>();
        for (Map<String,String> mp:mapIn)
        {
            list.addAll(mp.values());
        }

        return list;
    }

    private HashMap<String,List<Integer>> shuffleOut(List<String> randIn)
    {
        List<Integer> list = new ArrayList<>();
        HashMap<String,List<Integer>> map = new HashMap<>();

        String temp="";
        for (String mp:randIn)
        {
            if(isValueValid(mp))
            {
                String[] spl=mp.split(",");
                if(!temp.equals(spl[0]))
                {
                    temp=spl[0];
                    list=new ArrayList<>();
                }
                list.add(Integer.valueOf(spl[1]));
                map.put(temp,list);
            }

        }

        //StopWatch stopWatch = new StopWatch();
        long lg=0L;
        for(int i = 0; i<=2000000000;i++)
        {
           lg++;
        }
        /*//stopWatch.stop();
        System.out.println("THREAD Time: "+lg);*/
        return map;

    }

    private HashMap<String,List<Integer>> shuffleOutTemp(List<String> randIn)
    {
        List<Integer> list = new ArrayList<>();
        HashMap<String,List<Integer>> map = new HashMap<>();

        String temp="";
        for (String mp:randIn)
        {
            if(isValueValid(mp))
            {
                String[] spl=mp.split(",");
                if(!temp.equals(spl[0]))
                {
                    temp=spl[0];
                    list=new ArrayList<>();
                }
                list.add(Integer.valueOf(spl[1]));
                map.put(temp,list);
            }

        }

        return map;

    }

    private synchronized void reduce(HashMap<String,List<Integer>> redOut)
    {
        HashMap<String,Integer> mapR=new HashMap<>();
        for(Map.Entry<String,List<Integer>> red:redOut.entrySet())
        {
            mapR.put(red.getKey(),avgFunc(red.getValue()));
        }

        redu = mapR;

    }


    private class StartMapThread extends Thread
    {
        private HashMap<String,List<Integer>> tempList = new HashMap<>();

        public StartMapThread(HashMap<String,List<Integer>> list)
        {
            tempList = list;
        }

        public void run()
        {

            mapOut.putAll(tempList);//System.out.println(reduce(tempList));
            reduce(mapOut);

        }

    }


    private Integer avgFunc(List<Integer> vals)
    {
        Integer sumAvg=0;
        for(Integer vl:vals)
        {
            sumAvg+=vl;
        }

        return sumAvg/(vals.size()==0?1:vals.size());
    }

    private boolean isValueValid(final String value) {
        // We expect that the value is a String in the form of : State, Temperature. E.g. MP,77
        Pattern p = Pattern.compile("\\S\\S\\,\\d+");
        Matcher m = p.matcher(value);
        return m.matches();
    }


}
