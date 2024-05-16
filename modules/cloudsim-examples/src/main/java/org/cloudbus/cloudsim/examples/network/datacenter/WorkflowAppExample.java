package org.cloudbus.cloudsim.examples.network.datacenter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.HostEntity;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.network.datacenter.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class WorkflowAppExample {

	/** The vmList. */
	private static List<Vm> vmList;

	private static List<AppCloudlet> appCloudletList;

	private static NetworkDatacenter datacenter;

	private static NetworkDatacenterBroker broker;
	/**
	 * Creates main() to run this example.
	 * 
	 * @param args
	 *            the args
	 */
	public static void main(String[] args) {

		Log.printLine("Starting CloudSimExample1...");

		try {
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			datacenter = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			broker = createBroker();
			NetworkDatacenterBroker.setLinkDC(datacenter);
			// broker.setLinkDC(datacenter0);
			// Fifth step: Create one Cloudlet

			vmList = CreateVMs(datacenter.getId());

			appCloudletList = CreateAppCloudlets(1);

			// submit vm list to the broker

			broker.submitGuestList(vmList);
			broker.submitCloudletList(appCloudletList);

			// Sixth step: Starts the simulation
			CloudSim.startSimulation();

			CloudSim.stopSimulation();

			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);
			System.out.println("numberofcloudlet " + newList.size() + " Cached "
					+ NetworkDatacenterBroker.cachedcloudlet + " Data transfered "
					+ NetworkTags.totaldatatransfer);

			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}

	/**
	 * Creates the datacenter.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the datacenter
	 */
	private static NetworkDatacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine

		List<NetworkHost> hostList = new ArrayList<>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		// List<Pe> peList = new ArrayList<Pe>();

		int mips = 1;

		// 3. Create PEs and add these into a list.
		// peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to
		// store Pe id and MIPS Rating

		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int ram = 2048; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 10000;
		for (int i = 0; i < NetworkConstants.EdgeSwitchPort * NetworkConstants.AggSwitchPort
				* NetworkConstants.RootSwitchPort; i++) {
			// 2. A Machine contains one or more PEs or CPUs/Cores.
			// In this example, it will have only one core.
			// 3. Create PEs and add these into an object of PowerPeList.
			List<Pe> peList = new ArrayList<>();
			peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(1, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(2, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(3, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(4, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(5, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(6, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating
			peList.add(new Pe(7, new PeProvisionerSimple(mips))); // need to
			// store
			// PowerPe
			// id and
			// MIPS
			// Rating

			// 4. Create PowerHost with its id and list of PEs and add them to
			// the list of machines
			hostList.add(new NetworkHost(
					i,
					new RamProvisionerSimple(ram),
					new BwProvisionerSimple(bw),
					storage,
					peList,
					new VmSchedulerTimeShared(peList))); // This is our machine
		}

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
		// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<>(); // no SAN devices now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch,
				os,
				vmm,
				hostList,
				time_zone,
				cost,
				costPerMem,
				costPerStorage,
				costPerBw);

		// 6. Finally, we need to create a NetworkDatacenter object.
		NetworkDatacenter datacenter = null;
		try {
			datacenter = new NetworkDatacenter(
					name,
					characteristics,
					new VmAllocationPolicySimple(hostList),
					storageList,
					0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Create Internal Datacenter network
		CreateNetwork(datacenter);
		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 * 
	 * @return the datacenter broker
	 */
	private static NetworkDatacenterBroker createBroker() {
		NetworkDatacenterBroker broker = null;
		try {
			broker = new NetworkDatacenterBroker("Broker", 10);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 * 
	 * @param list
	 *            list of Cloudlets
	 * @throws IOException
	 */
	private static void printCloudletList(List<Cloudlet> list) throws IOException {
		int size = list.size();
		Cloudlet cloudlet;
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID"
				+ indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (Cloudlet value : list) {
			cloudlet = value;
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");
				Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent
						+ cloudlet.getGuestId() + indent + indent + dft.format(cloudlet.getActualCPUTime())
						+ indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}

	}

	/**
	 * Creates virtual machines in a datacenter
	 * @param datacenterId The id of the datacenter where to create the VMs.
	 */
	private static ArrayList<Vm> CreateVMs(int datacenterId) {
		ArrayList<Vm> vmList = new ArrayList<>();

		int numVM = datacenter.getHostList().size() * NetworkConstants.maxhostVM;
		for (int i = 0; i < numVM; i++) {
			int vmid = i;
			int mips = 1;
			long size = 10000; // image size (MB)
			int ram = 512; // vm memory (MB)
			long bw = 1000;
			int pesNumber = NetworkConstants.HOST_PEs / NetworkConstants.maxhostVM;
			String vmm = "Xen"; // VMM name

			// create VM
			Vm vm = new Vm(
					vmid,
					broker.getId(),
					mips,
					pesNumber,
					ram,
					bw,
					size,
					vmm,
					new NetworkCloudletSpaceSharedScheduler());

			vmList.add(vm);
		}

		return vmList;
	}

	private static ArrayList<AppCloudlet> CreateAppCloudlets(int apps) {
		ArrayList<AppCloudlet> cloudletList = new ArrayList<>();
		UniformDistr ufrnd = new UniformDistr(0, vmList.size(), 5);

		for(int appId = 0; appId < apps; appId++) {
			AppCloudlet app = new AppCloudlet(AppCloudlet.APP_Workflow, appId, 0, broker.getId());
			cloudletList.add(app);

			// Randomly select vm ids for the cloudlets within the app
			List<Integer> vmIds = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				vmIds.add((int) ufrnd.sample());
			}

			if (!vmIds.isEmpty()) {
				createTaskList(app, vmIds);
			}

		}

		return cloudletList;
	}

	static private void createTaskList(AppCloudlet appCloudlet, List<Integer> vmIdList) {
		long fileSize = NetworkConstants.FILE_SIZE;
		long outputSize = NetworkConstants.OUTPUT_SIZE;
		int memory = 100;
		UtilizationModel utilizationModel = new UtilizationModelFull();
		int i = 0;

		// Define cloudlets
		NetworkCloudlet cla = new NetworkCloudlet(
				NetworkConstants.currentCloudletId,
				0,
				1,
				fileSize,
				outputSize,
				memory,
				utilizationModel,
				utilizationModel,
				utilizationModel);
		NetworkConstants.currentCloudletId++;
		cla.setUserId(appCloudlet.getUserId());
		cla.submittime = CloudSim.clock();
		cla.currStagenum = -1;
		cla.setGuestId(vmIdList.get(i));
		appCloudlet.cList.add(cla);
		i++;

		NetworkCloudlet clb = new NetworkCloudlet(
				NetworkConstants.currentCloudletId,
				0,
				1,
				fileSize,
				outputSize,
				memory,
				utilizationModel,
				utilizationModel,
				utilizationModel);
		NetworkConstants.currentCloudletId++;
		clb.setUserId(appCloudlet.getUserId());
		clb.submittime = CloudSim.clock();
		clb.currStagenum = -1;
		clb.setGuestId(vmIdList.get(i));
		appCloudlet.cList.add(clb);
		i++;

		NetworkCloudlet clc = new NetworkCloudlet(
				NetworkConstants.currentCloudletId,
				0,
				1,
				fileSize,
				outputSize,
				memory,
				utilizationModel,
				utilizationModel,
				utilizationModel);
		NetworkConstants.currentCloudletId++;
		clc.setUserId(appCloudlet.getUserId());
		clc.submittime = CloudSim.clock();
		clc.currStagenum = -1;
		clc.setGuestId(vmIdList.get(i));
		appCloudlet.cList.add(clc);

		// Configure task stages within the cloudlets
		//
		cla.stages.add(new TaskStage(NetworkTags.EXECUTION, 0, 800, 0, memory, cla));
		cla.stages.add(new TaskStage(NetworkTags.WAIT_SEND, 1000, 0, 1, memory, clc));

		//
		clb.stages.add(new TaskStage(NetworkTags.EXECUTION, 0, 800, 0, memory, clb));
		clb.stages.add(new TaskStage(NetworkTags.WAIT_SEND, 1000, 0, 1, memory, clc));

		//
		clc.stages.add(new TaskStage(NetworkTags.WAIT_RECV, 1000, 0, 0, memory, cla));
		clc.stages.add(new TaskStage(NetworkTags.WAIT_RECV, 1000, 0, 1, memory, clb));
		clc.stages.add(new TaskStage(NetworkTags.EXECUTION, 0, 800, 2, memory, clc));
	}

	private static void CreateNetwork(NetworkDatacenter dc) {

		// Edge Switch
		EdgeSwitch edgeswitch = new EdgeSwitch("Edge0", NetworkConstants.EdgeSwitchPort, NetworkTags.EDGE_LEVEL,
					NetworkConstants.SwitchingDelayEdge, NetworkConstants.BandWidthEdgeHost, NetworkConstants.BandWidthEdgeAgg, dc);
		dc.Switchlist.put(edgeswitch.getId(), edgeswitch);

		for (HostEntity hs : dc.getHostList()) {
			NetworkHost hs1 = (NetworkHost) hs;

			edgeswitch.hostlist.put(hs.getId(), hs1);
			dc.HostToSwitchid.put(hs.getId(), edgeswitch.getId());
			hs1.sw = edgeswitch;
		}

	}
}
