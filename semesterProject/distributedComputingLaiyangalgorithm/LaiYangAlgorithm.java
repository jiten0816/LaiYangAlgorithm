package distributedComputingLaiyangalgorithm;

import java.text.SimpleDateFormat;

/**
 * This class holds the code for main execution for algorithms 
 * @author jitnakra 
 */

import java.util.*;

public class LaiYangAlgorithm {

	/** Global Variables */

	Scanner scan;
	String timeStampF;
	int valuetoCheck;
	int eventAmount = 0;
	int destProcessor = 0;
	int noOfProcessor = 0;
	int amount = 0;
	int latestAmount = 0;
	int timeSlots = 0;
	int snapdestProcessor = 0;
	int intialAmount = 0;

	String redKey;
	String receivechannelKey;
	String receiveredKey;
	String sendchannelKey;

	processCharacteristics pc;
	processCharacteristics updatedpc;
	channelCharac c;
	channelCharac cr;

	List<String> redMessageArrayP1 = new ArrayList<>();
	List<Integer> redMessageArrayP2 = new ArrayList<>();

	Map<String, processCharacteristics> hm = new LinkedHashMap<String, processCharacteristics>();
	Map<String, channelCharac> channelMap = new LinkedHashMap<String, channelCharac>();
	Map<String, Integer> channelRedMap = new LinkedHashMap<String, Integer>();

	private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void algoStart() {

		try {
			scan = new Scanner(System.in);

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			System.out.println("          Lai Yang Global Snapshot Algorithm");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			// The number of Processes are entered in this location
			System.out.println("Please enter total number of processes : ");
			noOfProcessor = scan.nextInt();
			System.out.println("====================INITIAL VALUES ->>> ENTER POSITIVE VALUES ONLY ================");
			for (int i = 0; i < noOfProcessor; i++) {
				System.out.println("\n" + "Please enter the intial value for Processor  " + (i + 1));
				amount = scan.nextInt();
				System.out.println("\n" + "**Initially, color of the Process P" + (i + 1) + " is WHITE");
				// Putting Process attributes
				pc = new processCharacteristics(i + 1, amount, Color.WHITE, 0, 0);
				hm.put("Process" + (i + 1) + "t0", pc);
				intialAmount += hm.get("Process" + (i + 1) + "t0").amount;
			}

			System.out.println("\n" + "========================Initialization completed================");

			System.out.println("\n" + "+ Processes Initialized Successfully. Intial system Amount is " + intialAmount);

			System.out.println("\n" + "---------------------*********************************------------");

			// Display Process map values

			System.out.println("\n" + "************************PROCESS SUMMARY************************");
			hm.forEach((k, v) -> System.out.println("\n" + "Process value : " + k + "\n" + "Summary :" + v));

			System.out.println(
					"===================TOTAL NUMBER OF TIMESTAMPS ->>> ENTER POSITIVE VALUES ONLY=========================");
			System.out.println("\n" + "Enter the total number of Time intervals");
			timeSlots = scan.nextInt();

			for (int i = 0; i < noOfProcessor; i++) {
				latestAmount = hm.get("Process" + (i + 1) + "t0").amount;

				for (int j = 1; j < timeSlots; j++) {
					System.out.println(
							"\n" + "***************Entering details for Process P" + (i + 1) + "**************");
					System.out.println("\n" + "Please enter details for Timeslots: t" + j + "<->" + "t" + (j + 1));
					System.out.println('\n'
							+ "**********************************User Selection for Events************************************************");
					System.out.println("(A) To Trigger Send Event : Enter 1 " + '\n'
							+ "(B) To Trigger Receive Event : Enter 2" + "\n" + "(C) In case of No Event : Enter 3");

					int eventDecision = scan.nextInt();
					eventSelection(eventDecision, i, j);
				}
			}
			captureSnapshot();
		} catch (InputMismatchException e) {
			if (!scan.hasNextInt())
				throw new customLaiYangException("\n"
						+ "+----------------Oops, Invalid Value was entered , please enter positive values such as 1,2,3 ---------+",
						e);
		}
	}

	private void eventSelection(int eventDecision, int processNumber, int timeSlot) {

		switch (eventDecision) {
		case 1:
			System.out.println("Enter the amount which needs to be transferred to the destination processor");
			eventAmount = scan.nextInt();
			System.out.println("Enter the Destination Processor number");
			destProcessor = scan.nextInt();
			updatedpc = new processCharacteristics((processNumber + 1), (latestAmount - eventAmount), Color.WHITE,
					timeSlot, timeSlot + 1);
			latestAmount = latestAmount - eventAmount;
			hm.put("Process" + (processNumber + 1) + "t" + timeSlot, updatedpc);

			System.out.println("\n" + "************************PROCESS SUMMARY************************");
			hm.forEach((k, v) -> System.out.println("\n" + "Process value : " + k + "\n" + "Summary :" + v));

			/*
			 * Channel ---creation of Key and assigning amount with Color
			 */
			sendchannelKey = "P" + (processNumber + 1) + "C" + (processNumber + 1) + "" + destProcessor + "t"
					+ timeSlot; // P1C12t1 //Rc 21

			channelMap.put(sendchannelKey, new channelCharac(eventAmount, Color.WHITE));

			System.out.println("*********************CHANNEL SUMMARY*************************");

			channelMap.forEach((k, v) -> System.out.println("Channel details : " + k + "\n" + "Summary : " + v));
			break;
		case 2:
			System.out.println("Amount received at processor" + (processNumber + 1)); // i=1
			eventAmount = scan.nextInt();
			System.out.println("Enter the sender Processor number"); // 1
			int senderProcessor = scan.nextInt();
			updatedpc = new processCharacteristics((processNumber + 1), (latestAmount + eventAmount), Color.WHITE,
					timeSlot, timeSlot + 1);
			latestAmount = latestAmount + eventAmount;
			hm.put("Process" + (processNumber + 1) + "t" + timeSlot, updatedpc);

			System.out.println("\n" + "************************PROCESS SUMMARY************************");
			hm.forEach((k, v) -> System.out.println("\n" + "Process value : " + k + "\n" + "Summary :" + v));

			// creation of Key and assigning amount with Color
			receivechannelKey = "P" + (processNumber + 1) + "C" + senderProcessor + (processNumber + 1) + "t"
					+ timeSlot; // C21t1 // C12t1

			channelMap.put(receivechannelKey, new channelCharac(eventAmount, Color.WHITE));

			System.out.println("*********************CHANNEL SUMMARY*************************");
			channelMap.forEach((k, v) -> System.out.println("Channel details : " + k + "\n" + "Summary : " + v));
			break;
		case 3:

			updatedpc = new processCharacteristics((processNumber + 1), (latestAmount), Color.WHITE, timeSlot,
					timeSlot + 1);
			hm.put("Process" + (processNumber + 1) + "t" + timeSlot, updatedpc);
			System.out.println("\n" + "************************PROCESS SUMMARY************************");
			hm.forEach((k, v) -> System.out.println("\n" + "Process value : " + k + "\n" + "Summary :" + v));

			break;
		}
	}

	private void captureSnapshot() {

		
		System.out
		.println("\n" + "==============================CAPTURE SNAPSHOT======================================");
		System.out.println(
				'\n' + "Please enter Y to initiate the process for capturing snapshot else N to quit the algorithm");
		String flag = scan.next();
		if (flag.equalsIgnoreCase("Y")) {
			System.out
			.println("**************************INITIATOR PROCESS KICKS IN **************************" + "\n");

			System.out.println(
					"Let's provide the processes values where snapshot is to be triggered and time instant values");

			System.out.println("Please specify the first process number which is intiating the algorithm ");
			int snapSourceProcessor = scan.nextInt();

			//			System.out.println("\n" + "Please specify the other process number :");
			//			int snapdestProcessor = scan.nextInt();

			System.out.println("\n" + "Please enter the timeslot for process P" + snapSourceProcessor
					+ " at which snapshot is to be captured : ");

			int snapSourceTime = scan.nextInt();

			System.out.println("\n" + "*******************PROCESS P" + snapSourceProcessor
					+ " TURNS RED & RECORDS LOCAL SNAPSHOT***********");

			// Red Message Calculation
			if ((timeSlots - snapSourceTime) > 1) {

				for (Map.Entry<String, channelCharac> entry : channelMap.entrySet()) {
					for (int k = snapSourceTime; k < timeSlots; k++) {

						int processF = Integer.parseInt(entry.getKey().substring(1, 2));
						//only P1 entries
						if (processF < 2 && processF > 0) {
							redKey = entry.getKey().substring(0, 6);
							redKey = redKey + k;
						}

						// redKey= "P" + snapSourceProcessor + "C" + snapSourceProcessor +
						// snapdestProcessor + "t" + k;
						// 3 <7 , white -P1C12t3 =10 - Red

						if (channelMap.containsKey(redKey)) {

							channelMap.put(redKey, new channelCharac(channelMap.get(redKey).amount, Color.RED));
							channelRedMap.put(redKey, channelMap.get(redKey).amount);
						}

					}
					break;
				}
			} else if ((timeSlots - snapSourceTime) == 0) {
				System.out.println("*************No Red Messages are present in Process P1****************");
			}

			// Process p1 channel
			System.out.println("\n"+"*********************PROCESS P" + snapSourceProcessor
					+ " --->  RED MESSAGES CHANNEL SUMMARY*************************");
			channelMap.forEach((kr, v) -> {
				if (v.getColor() == Color.RED) {
					redMessageArrayP1.add(kr);
					System.out.println("Updated Channel details :  " + kr + "\n" + "values" + v);

				}
			});

			//p1c12t2	 
			if (redMessageArrayP1.size() > 0)

			{
				System.out.println("\n" + "******************************From Red Message Summary printed above for process P"  +snapSourceProcessor +"| " +"\n"+ "-------->>>>>> choose and Enter the amount which has been recorded as RECEIVED EVENT AMOUNT in destination process as per time intervals : "  );
				valuetoCheck = scan.nextInt();
				System.out.println("\n" + "Message Amount entered is "  +valuetoCheck);
				
				//valuetoCheck = channelMap.get(redMessageArrayP1.get(0)).amount;
				
			}// P1 first Red Value to be searched in
				// P2 timestamp value --10

				// System.out.println("First Red Message "+valuetoCheck);

				// Code for value to Key in Process 10 key in Process p2 
			
			System.out.println("\n"+"==========================================================================================");
				for (Map.Entry<String, channelCharac> entry : channelMap.entrySet()) {

					int processF = Integer.parseInt(entry.getKey().substring(1, 2));
//only 2 proces filtering 
					if (processF > 1 && entry.getValue().amount == (valuetoCheck)) {
						System.out.println("The key for value " + valuetoCheck + " is " + entry.getKey());
						timeStampF = entry.getKey();
						break;
					}
					/*else throw new customLaiYangException("\n"
							+ "+----------------Oops, Invalid Value was entered , please enter positive values such as 1,2,3 ---------+");
//						System.out.println("Invalid entries by user, Please recheck the send and Receive events values for processes");
					//}
				}*/

			}
//p2 -> timestamp deduction like 4,5 based on user entry from channel map as where it was received
			if (!timeStampF.isEmpty()) {
				timeStampF = timeStampF.toString().substring(6, 7);

			} 
			//process name dynamic for destination snapshot
			String redMessageArrayF = redMessageArrayP1.toString().substring(5, 6);

			System.out.println("\n" + "+-----------------------Red Message of amount " + valuetoCheck + " will reach PROCESS " + redMessageArrayF
					+ " at TIMESLOT " + timeStampF
					+ " and it immediately records its local snapshot before processing this message.");

			int snapdestTime = Integer.parseInt(timeStampF);
			snapdestProcessor = Integer.parseInt(redMessageArrayF);
			
			System.out.println("\n" + "*************************************PROCESS P" + snapdestProcessor
					+ " TURNS RED************************************");
			
			System.out.println("\n" + "# Process P " +snapdestProcessor +" shares the history of white messages sent or received along each channel to INIATOR Process");
			 
			int localAmountP1 = hm.get("Process" + snapSourceProcessor + "t" + (snapSourceTime - 1)).amount;
			int add1 = 0;
			int sub1 = 0;

			for (int ks = 0; ks < snapSourceTime; ks++) {
				// c12
				if (channelMap.containsKey(
						"P" + snapSourceProcessor + "C" + snapSourceProcessor + snapdestProcessor + "t" + ks)) {
					add1 += channelMap.get("P" + snapSourceProcessor + "C" + snapSourceProcessor + snapdestProcessor
							+ "t" + ks).amount;

				}
				// P1 --c21
				if (channelMap.containsKey(
						"P" + snapSourceProcessor + "C" + snapdestProcessor + snapSourceProcessor + "t" + ks)) {
					sub1 += channelMap.get("P" + snapSourceProcessor + "C" + snapdestProcessor + snapSourceProcessor
							+ "t" + ks).amount;
				}

			}

			// input for Second Process

			int localAmountP2 = hm.get("Process" + snapdestProcessor + "t" + (snapdestTime - 1)).amount;

			int add2 = 0;
			int sub2 = 0;
			for (int kd = 0; kd < snapdestTime; kd++) {

				// c21=send
				if (channelMap.containsKey(
						"P" + snapdestProcessor + "C" + snapdestProcessor + snapSourceProcessor + "t" + kd)) {
					add2 += channelMap.get(
							"P" + snapdestProcessor + "C" + snapdestProcessor + snapSourceProcessor + "t" + kd).amount;

				}
				// Receive C12
				if (channelMap.containsKey(
						"P" + snapdestProcessor + "C" + snapSourceProcessor + snapdestProcessor + "t" + kd)) {
					sub2 += channelMap.get(
							"P" + snapdestProcessor + "C" + snapSourceProcessor + snapdestProcessor + "t" + kd).amount;
				}
			}

			int final1 = add1 - sub2;
			int final2 = add2 - sub1;

			/*
			 * //to be removed ---- if ((timeSlots - snapdestTime) > 1) { for (int d =
			 * snapdestTime; d < timeSlots; d++) {
			 * 
			 * receiveredKey= "P" + snapSourceProcessor + "C" + snapdestProcessor +
			 * snapSourceProcessor + "t" + d; // 3 <7 , white -P1C12t3 =10 - Red
			 * 
			 * channelMap.put(receiveredKey, new
			 * channelCharac(channelMap.get(receiveredKey).amount, Color.RED));
			 * 
			 * channelMap.forEach((k1,v)-> { if(v.getColor()==Color.RED)
			 * System.out.println("Updated Channel details are "+ k1 + "values" +v);
			 * 
			 * }); } } else if ((timeSlots - snapdestTime) == 0) { System.out.
			 * println("**************No Red Messages are present in Process P2***********"
			 * ); }
			 */

			System.out.println("\n" + "+========================Local Snapshot Summary for Process "
					+ snapSourceProcessor + "=======================+");
			System.out.println("# Local amount before Process 1 turns red is :" + localAmountP1);
			System.out.println(
					"# White Message/s sent by P"+snapSourceProcessor+" on " + "C" + snapSourceProcessor + snapdestProcessor + ": " + add1);
			System.out.println("# White Message/s received by P"+snapSourceProcessor+" on " + "C" + snapdestProcessor +snapSourceProcessor + ": " + sub1);
			// System.out.println("# Red Messages rresent in Process 1 are" +
			// redMessageArrayP1);

			System.out.println("\n" + "+========================Local Snapshot Summary for Process " + snapdestProcessor
					+ "=======================+");
			System.out.println("# Local amount before Process 1 turns red is :" + localAmountP2);
			System.out.println(
					"# White Message/s sent by P"+snapdestProcessor+" on " + "C" + snapdestProcessor + snapSourceProcessor + ": " + add2);
			System.out.println("# White Message/s received by P"+snapdestProcessor+" on" + "C" +snapSourceProcessor+ snapdestProcessor + " : " + sub2);
			// System.out.println("# Red Messages present in Process 2 are" +
			// redMessageArrayP2);

			System.out.println(
					"\n" + "+***********************************GLOBAL SNAPSHOT*************************************+");

			System.out.println("# State of channel S" + "C" + snapSourceProcessor + snapdestProcessor + " : " + final1);
			System.out.println("# State of channel S" + "C" + snapdestProcessor + snapSourceProcessor + " : " + final2);
			int finalAmount = localAmountP1 + localAmountP2 + final1 + final2;
			System.out.println("# Final global snapshot value : " + finalAmount);
			if (intialAmount == finalAmount) {
				System.out.println(
						"# Initial system value is EQUAL to snapshot value hence it is a consistent global state");
			} else {
				System.out.println(
						"# Initial system value is NOT-EQUAL to snapshot value hence it is a Inconsistent global state");

			}
			System.out.println(
					"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			System.out.println("                             End of Algorithm");
			System.out.println(
					"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		} else {
			System.out.println("User has aborted the algorithm before capturing snapshot");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			System.out.println("          End of Algorithm");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		}
	
	}
}

