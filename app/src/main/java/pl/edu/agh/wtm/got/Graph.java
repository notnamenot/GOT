package pl.edu.agh.wtm.got;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.wtm.got.models.GOTPoint;
import pl.edu.agh.wtm.got.models.Route;
import pl.edu.agh.wtm.got.models.Subroute;

// http://www.algorytm.org/algorytmy-grafowe/algorytm-dijkstry/dijkstra-j.html?fbclid=IwAR0EXm7R50_i6WWaFDYGyPc1FioUTMUvOBDnXbfD7arz8Zi1li6PWHlMxaU
public class Graph {
        //TODO jest przeszukiwanie wgłą, lepsze byłoby wszerz
//    private Map<GOTPoint, Set<GOTPoint>> adjList ;
    private Map<Integer, Set<Integer>> adjList ;

//    private int number_of_got_points; // liczba wierzchołków vertices

    private ArrayList<ArrayList<Integer>> foundPaths;

    private ArrayList<Route> foundRoutes;

    private GOTdao dao;

    public Graph(int from, int to, int mountainChainId, Activity activity)
    {
        dao = new GOTdao(activity);

        foundPaths = new ArrayList<ArrayList<Integer>> (10);
        foundRoutes = new ArrayList<>(10);

        List<Subroute> subrouteList = dao.getSubroutes(mountainChainId);

        int gotPointsCnt = dao.getGotPointsCnt(mountainChainId);
//        this.number_of_got_points = gotPointsCnt;
//        System.out.println("number_of_got_points" + number_of_got_points);

        adjList = new HashMap<>();
        for (Subroute subroute : subrouteList) {

            Set<Integer> neighbors = adjList.get(subroute.getFrom());
            if(neighbors == null) {
                neighbors = new HashSet<Integer>();
                adjList.put(subroute.getFrom(),neighbors);
            }
            neighbors.add(subroute.getTo());
        }


        System.out.println("Following are all different paths from "+ from +" to "+ to);
        printAllPaths(from, to);


        System.out.println("Finito");
//        for (ArrayList<Integer> integers : foundPaths) for (Integer integer : integers)  System.out.println("INTEGER " + integer);

        createRoutesFromFoundPaths();

    }


    public void printAllPaths(int source, int destination)
    {
        ArrayList<Integer> isVisited = new ArrayList<>(10);
//        boolean[] isVisited = new boolean[number_of_got_points+1];
        ArrayList<Integer> pathList = new ArrayList<>(10);

        //add source to path[]
        pathList.add(source);

        //Call recursive utility
        printAllPathsUtil(source, destination, isVisited, pathList);
    }

    private void printAllPathsUtil(Integer u, Integer d,
                                   //boolean[] isVisited,
                                   List<Integer> isVisited,
                                   ArrayList<Integer> localPathList) {

        // Mark the current node
//        isVisited[u] = true;
        isVisited.add(u);

        if (u.equals(d)) // koniec trasy
        {
            ArrayList<Integer> newList = new ArrayList<>(localPathList);
            foundPaths.add(newList);
            System.out.println("localPathList " + localPathList);
            // if match found then no need to traverse more till depth
//            isVisited[u]= false;
            isVisited.remove(u);
            return ;
        }

        // Recur for all the vertices
        // adjacent to current vertex
         if (adjList.get(u) != null) {
             for (Integer i : adjList.get(u)) {
//                 if (!isVisited[i]) {
                 if (!isVisited.contains(i)) {
                     // store current node
                     // in path[]
                     localPathList.add(i);
                     printAllPathsUtil(i, d, isVisited, localPathList);

                     // remove current node
                     // in path[]
                     localPathList.remove(i);
                 }
             }
         }

        // Mark the current node
//        isVisited[u] = false;
        isVisited.remove(u);
    }

    private void createRoutesFromFoundPaths() {

        boolean isFirst = true;
        int prevGotPoint = 0;
        for (ArrayList<Integer> foundPathList : foundPaths) {

            List<Subroute> subroutes = new ArrayList<>();
            List<GOTPoint> gotPoints = new ArrayList<>();

            for (Integer gotPointId : foundPathList)
            {
//                System.out.println("pathList " + foundPathList);
//                System.out.println("gotPointId from path " + gotPointId);
                if (isFirst) {
                    prevGotPoint = gotPointId;
                    isFirst = false;
                }
                else {
                    subroutes.add(dao.getSubroute(prevGotPoint, gotPointId));
                    prevGotPoint = gotPointId;
                }

                gotPoints.add(dao.getGOTPoint(gotPointId));

            }
            isFirst = true;

            int points = 0;
            double length = 0;
            int time = 0;
            int ups = 0;
            int downs = 0;
            for (Subroute subroute : subroutes) {
                points += subroute.getPoints();
                length += subroute.getLength();
                time += subroute.getTime();
                ups += subroute.getUps();
                downs += subroute.getDowns();
            }

            length = (double) Math.round(length*100) / 100; // round up to 2 decimal places

            Route route = new Route(0,points,length,time,ups,downs,gotPoints,subroutes);
            foundRoutes.add(route);
        }

    }


    public ArrayList<Route> getFoundRoutes() {
        return foundRoutes;
    }

    public void setFoundRoutes(ArrayList<Route> foundRoutes) {
        this.foundRoutes = foundRoutes;
    }

}
