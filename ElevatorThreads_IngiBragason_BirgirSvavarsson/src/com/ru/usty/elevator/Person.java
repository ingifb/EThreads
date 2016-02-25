package com.ru.usty.elevator;

public class Person implements Runnable{
	
	int sourcef, destinationf;
	public Person(int sourcef, int destinationf)
	{
		this.sourcef = sourcef;
		this.destinationf = destinationf;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//Skoda fyrirlestur aftur fyrir elevatorWaitMutex
			ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.elevatorIn.acquire(); // bida eftir ad losni
			ElevatorScene.elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(sourcef);
		System.out.println("Person thread in elevator");
	}

}
