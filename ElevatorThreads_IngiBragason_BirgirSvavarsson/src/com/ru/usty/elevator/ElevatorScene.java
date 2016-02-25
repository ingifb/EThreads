package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ElevatorScene {
       
       //hafa ALLAR semaphorur her
       public static Semaphore elevatorIn;
       //public static Semaphore elevatorOut;
       public static Semaphore personCountMutex;
       public static Semaphore elevatorWaitMutex;
       public static Semaphore elevatorThinkMutex;
       
       public static ElevatorScene scene;
       
       public static boolean elevatorsMayDie;

       //TO SPEED THINGS UP WHEN TESTING,
       //feel free to change this.  It will be changed during grading
       public static final int VISUALIZATION_WAIT_TIME = 300;  //milliseconds

       private int numberOfFloors;
       private int numberOfElevators;
       
       public static final int ELEVATOR_SIZE = 6;
       
       public int numberOfSpacesInElevator;
       public int currentFloor;
       public static int totalPeopleWaiting;
       
       private Thread elevatorThread = null;

       ArrayList<Integer> personCount; //use if you want but
                                                              //throw away and
                                                              //implement differently
                                                              //if it suits you

       //Base function: definition must not change
       //Necessary to add your code in this one
       public void restartScene(int numberOfFloors, int numberOfElevators) {

              /**
              * Important to add code here to make new
              * threads that run your elevator-runnables
              * 
               * Also add any other code that initializes
              * your system for a new run
              * 
               * If you can, tell any currently running
              * elevator threads to stop
              */
              
              elevatorsMayDie = true;
              
              if(elevatorThread != null)
              {
                     if(elevatorThread.isAlive())
                     {
                           try {
                                  elevatorThread.join();
                           } catch (InterruptedException e) {
                                  // TODO Auto-generated catch block
                                  e.printStackTrace();
                           }
                     }
              }
              elevatorsMayDie = false;
              
              scene = this;
              elevatorIn = new Semaphore(0);
              //elevatorOut = new Semaphore(0);
              personCountMutex = new Semaphore(1);
              elevatorWaitMutex = new Semaphore(1);
              elevatorThinkMutex = new Semaphore(1);
              
              
              //why so loopy?
              /*
              for(int i = 0; i < numberOfElevators; i++)
              {      
                     System.out.println("in for loop");
                     Thread thread = new Thread(new Elevator());
                     thread.start();
              }
              */
                           
              //setja tetta inni Elevator classann:
              
              elevatorThread = new Thread(new Runnable()
        {
               public void run() 
               {
                     while(true)
                     {
                     try {
                             Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
                      } catch (InterruptedException e1) {
                             e1.printStackTrace();
                      }
                            while(totalPeopleWaiting > 0)
                            {
                                     if(totalPeopleWaiting > ELEVATOR_SIZE)
                                   {
                                          while(numberOfSpacesInElevator != ELEVATOR_SIZE)
                                          {                                                      
                                                 try 
                                                 {
                                                        elevatorThinkMutex.acquire();
                                                        for(int i = 0; i < 6; i++)
                                                        {   
                                                               ElevatorScene.elevatorIn.release();
                                                               numberOfSpacesInElevator++;                                                        
                                                        }
                                                        elevatorThinkMutex.release();
                                                 } catch (InterruptedException e) 
                                                 {
                                                        e.printStackTrace();
                                                 }                                                
                                                 try 
                                                 {
                                                        Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
                                                 } catch (InterruptedException e1) 
                                                 {
                                                        e1.printStackTrace();
                                                 }
                                          }
                                   }
                                   else
                                   {      
                                            try 
                                              {
                                                     elevatorThinkMutex.acquire();
                                                     for(int i = 0; i < totalPeopleWaiting; i++)
                                                     {   
                                                            ElevatorScene.elevatorIn.release();
                                                            numberOfSpacesInElevator++;                                                        
                                                     }
                                                     elevatorThinkMutex.release();
                                              } catch (InterruptedException e) 
                                              {
                                                     e.printStackTrace();
                                              } 
                                          try 
                                          {
                                                 Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
                                          } catch (InterruptedException e1) 
                                          {
                                                 e1.printStackTrace();
                                          }
                                   }
                                   currentFloor++;
                                   while(numberOfSpacesInElevator > 0 )
                                   {
                                          for(int i = 0; i < numberOfSpacesInElevator; i++)
                                          {
                                                 System.out.println("person leaving now");
                                                 try 
                                                 {
                                                        Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
                                                 } catch (InterruptedException e) 
                                                 {
                                                        e.printStackTrace();
                                                 }
                                                 numberOfSpacesInElevator--;
                                                 totalPeopleWaiting--;
                                          }
                                   }
                                   currentFloor--;
                            }
                            elevatorsMayDie = false;
                     }
               }
       });
        elevatorThread.start();

              
              /*
              Thread[] elevatorThreads = new Thread[numberOfElevators]; 
              
              for(int i = 0; i < numberOfElevators; i++)
              {
                     Elevator elevatorInstance = new Elevator();
                     elevatorThreads[i] = new Thread(elevatorInstance);
                     elevatorThreads[i].start();
              }
              
              */
              
              this.numberOfFloors = numberOfFloors;
              this.numberOfElevators = numberOfElevators;
              
              personCount = new ArrayList<Integer>();
              for(int i = 0; i < numberOfFloors; i++) {
                     this.personCount.add(0);
              }
              

       }

       //Base function: definition must not change
       //Necessary to add your code in this one
       public Thread addPerson(int sourceFloor, int destinationFloor) {

              /**
              * Important to add code here to make a
              * new thread that runs your person-runnable
              * 
               * Also return the Thread object for your person
              * so that it can be reaped in the testSuite
              * (you don't have to join() yourself)
              */

              Thread thread = new Thread(new Person(sourceFloor, destinationFloor));
              thread.start();

              incrementNumberOfPeopleWaitingAtFloor(sourceFloor);
              

              return thread;
       }

       //Base function: definition must not change, but add your code
       public int getCurrentFloorForElevator(int elevator) {
              
              int i = currentFloor;
              return i;
       }

       //Base function: definition must not change, but add your code
       public int getNumberOfPeopleInElevator(int elevator) {
              /*
              switch(elevator) {
              case 1: return 1;
              case 2: return 4;
              default: return 0;
              }
              */
              int i = 0;
              try {
                     ElevatorScene.personCountMutex.acquire();
                           i = numberOfSpacesInElevator;
                     ElevatorScene.personCountMutex.release();
              } catch (InterruptedException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }      
              return i;
       }
              
       //Setja increment fall a people in elevator eins og waiting at floor, skoda fyrirlestur around 60 min

       //Base function: definition must not change, but add your code
       public int getNumberOfPeopleWaitingAtFloor(int floor) {
              return personCount.get(floor);
       }
       
       public void decrementNumberOfPeopleWaitingAtFloor(int floor)
       {
              try {
                     ElevatorScene.personCountMutex.acquire();
                           personCount.set(floor, (personCount.get(floor) - 1));
                     ElevatorScene.personCountMutex.release();
              } catch (InterruptedException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }
       }
       
       public void incrementNumberOfPeopleWaitingAtFloor(int floor)
       {
              try {
                     ElevatorScene.personCountMutex.acquire();
                           personCount.set(floor, (personCount.get(floor) + 1));
                           totalPeopleWaiting++;
                     ElevatorScene.personCountMutex.release();
              } catch (InterruptedException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }
       }

       //Base function: definition must not change, but add your code if needed
       public int getNumberOfFloors() {
              return numberOfFloors;
       }

       //Base function: definition must not change, but add your code if needed
       public void setNumberOfFloors(int numberOfFloors) {
              this.numberOfFloors = numberOfFloors;
       }

       //Base function: definition must not change, but add your code if needed
       public int getNumberOfElevators() {
              return numberOfElevators;
       }

       //Base function: definition must not change, but add your code if needed
       public void setNumberOfElevators(int numberOfElevators) {
              this.numberOfElevators = numberOfElevators;
       }

       //Base function: no need to change unless you choose
       //                         not to "open the doors" sometimes
       //                         even though there are people there
       public boolean isElevatorOpen(int elevator) {

              return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
       }
       
       //Base function: no need to change, just for visualization
       //Feel free to use it though, if it helps
       public boolean isButtonPushedAtFloor(int floor) {

              return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
       }

}

