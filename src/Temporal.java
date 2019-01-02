public class Temporal{

	//
	// This program is free software: you can redistribute it and/or modify
	// it under the terms of the GNU Lesser General Public License as published
	// by
	// the Free Software Foundation, either version 3 of the License, or
	// (at your option) any later version.
	//
	// This program is distributed in the hope that it will be useful,
	// but WITHOUT ANY WARRANTY; without even the implied warranty of
	// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the	
	// GNU Lesser General Public License for more details.
	//
	// You should have received a copy of the GNU Lesser General Public License
	// along with this program. If not, see http://www.gnu.org/licenses/.
	//

	// 1. General variables
//	private int packageID = 0; // Package ID
//	private int numSent = 0; // Sent packages
//	private int numReceived = 0; // Received packages
//	private float totalDistance = 0; // Total distance
//	private float lastAccelerationPlatoon = 0; // Last acceleration calculated
//												// with CACC
//	private float localLeaderAcceleration = 0; // Acceleration of leader
//	private float localLeaderSpeed = 0; // // Speed of leader
//
//	// 2 . Platoon's parameters (CACC)
//	private float alpha1 = 0.5f;
//	private float alpha2 = 0.5f;
//	private float alpha3 = 0.3f;
//	private float alpha4 = 0.1f;
//	private float alpha5 = 0.04f;
//	private float alphaLag = 0.8f;
//	private float length_vehicle_front = 2f;
//	private float desiredSpacing = 2f;
//	private boolean beaconingEnabled = false;
//
//
public Temporal() {

	}

//	public float getAccelerationPlatoon() {
//		// 1. PREPARE
//
//		// List without duplicates
//
//		float distanceBetweenCurrentAndFront;
//
//		// Add NodeInfo objects to the list without duplicates
//
//		boolean existeInfo = false;
//
//		// 2. START PLATOON
//
//		// Calculate distance between nodes and current to determine the nearest
//		// to the current
//		Agent nearestNode = null;
//		Agent leaderNode = null;
//
//		// b. Get information from the nearest node in the front
//		float rel_speed_front;
//		float spacing_error;
//		float nodeFrontAcceleration;
//
//		if (nearestNode != null) {
//			// Calculate relative speed to the node in front
//			rel_speed_front = getSpeed() - nearestNode.getSpeed();
//
//			// Calculate spacing error
//			distanceBetweenCurrentAndFront = nearestNode
//					.distanceFrontToCurrent();
//			spacing_error = -distanceBetweenCurrentAndFront
//					+ length_vehicle_front + desiredSpacing;
//
//			nodeFrontAcceleration = nearestNode.getAcceleration();
//
//		} else {
//			rel_speed_front = 0;
//			spacing_error = 0;
//			nodeFrontAcceleration = 0;
//		}
//
//		// c. Get information from leader
//		float leaderAcceleration;
//		float leaderSpeed;
//
//		if (leaderNode == null) {
//			// We use the data coming with beaconing
//			if (nearestNode != null && beaconingEnabled == true) {
//				leaderAcceleration = leaderAgent.getAcceleration();
//				localLeaderAcceleration = leaderAgent.getAcceleration();
//				leaderSpeed = leaderAgent.getSpeed();
//				localLeaderSpeed = leaderAgent.getSpeed();
//			} else {
//				leaderAcceleration = 0;
//				leaderSpeed = 0;
//				localLeaderAcceleration = 0;
//				localLeaderSpeed = 0;
//			}
//
//		} else {
//			leaderAcceleration = leaderAgent.getAcceleration();
//			leaderSpeed = leaderAgent.getSpeed();
//		}
//
//		// Print data for calculation
//		// cout << "Node[" << getMyAddress() << "]: Platoon parameters" << endl;
//		// cout << "Distance to Vehicle in Front=" <<
//		// distanceBetweenActualAndFront << endl;
//		// cout << "Vehicle in Front Acceleration=" << nodeFrontAcceleration <<
//		// endl;
//		// cout << "Leader Acceleration=" << leaderAcceleration << endl;
//		// cout << "Relative speed vehicule in front=" << rel_speed_front <<
//		// endl;
//		// cout << "Leader speed=" << leaderSpeed << endl;
//		// cout << "My speed=" << getMySpeed() << endl;
//		// cout << "Spacing Error=" << spacing_error << endl;
//
//		// d. Calculate (Acceleration desired) A_des
//		float A_des = alpha1 * nodeFrontAcceleration + alpha2
//				* leaderAcceleration - alpha3 * rel_speed_front - alpha4
//				* (getSpeed() - leaderSpeed) - alpha5 * spacing_error;
//
//		// e. Calculate desired acceleration adding a delay
//		float A_des_lag = (alphaLag * A_des)
//				+ ((1 - alphaLag) * lastAccelerationPlatoon);
//		lastAccelerationPlatoon = A_des_lag;
//		return A_des_lag;
//	}

}
