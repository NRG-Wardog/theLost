import asyncio
from bleak import BleakScanner
import pyrebase

# --------------------------------------------------------------------------------
# Firebase configuration:
# --------------------------------------------------------------------------------
# IMPORTANT: You must provide the exact 'databaseURL' for your Firebase project.
# You can find it in your Firebase console under "Realtime Database".
# The example below uses a placeholder URL (thelost-78470-default-rtdb.firebaseio.com).
# Make sure to replace it with your actual database URL.

firebaseConfig = {
    'apiKey': "api_key",
    'authDomain': "thelost-78470.firebaseapp.com",
    'databaseURL': "https://thelost-78470-default-rtdb.firebaseio.com/",  # Replace with your valid Realtime Database URL
    'projectId': "thelost-78470",
    'storageBucket': "thelost-78470.firebasestorage.app",
    'messagingSenderId': "973550724894",
    'appId': "1:973550724894:web:e61ab822bbbd990ddb0b0f",
    'measurementId': "G-JRBLHFERH3"
}

# Initialize the Firebase app using Pyrebase
firebase = pyrebase.initialize_app(firebaseConfig)
# Access the Realtime Database object
F_DB = firebase.database()

def calculate_distance(rssi, A=-59, n=2.0):
    """
    Calculate an approximate distance based on the Received Signal Strength Indicator (RSSI).

    :param rssi: The RSSI value (in dBm) from the BLE device.
    :param A: The Tx Power (signal strength at 1 meter). Default is -59 dBm.
    :param n: The signal propagation exponent. Default is 2.0 for free space/clear line-of-sight.
    :return: An approximate distance in meters, or None if rssi is None.
    """
    if rssi is None:
        return None
    # Formula: distance = 10 ^ ((A - rssi) / (10 * n))
    return 10 ** ((A - rssi) / (10 * n))

async def scan_specific_device(target_address="51:19:87:59:01:D4"):
    """
    Continuously scans for a BLE device with a specified MAC address (target_address).
    When the target device is found, estimates the distance from its RSSI and pushes data to Firebase.

    :param target_address: The BLE MAC address (string) of the device you want to find.
    """
    while True:
        try:
            found_devices = await BleakScanner.discover()
            target_devices = [device for device in found_devices if device.address.lower() == target_address.lower()]
            for device in found_devices:
                print("Found device:", device.address,"fat",device.rssi)

            # If the target device is found, calculate the distance and push data to Firebase
            if target_devices:
                device = target_devices[0]
                distance = calculate_distance(device.rssi)
                if distance is not None:
                    # Decide how you want to categorize the distance
                    if distance < 5:
                        data = {"status": "close","distance_meters": round(distance, 2)}
                    else:
                        data = {"status": "far","distance_meters": round(distance, 2)}
                    F_DB.child("alart").push(data)
                    print("Pushed to Firebase:", data)

        except Exception as e:
            print(f"Error during scan: {e}")

        # Sleep for 1 second before scanning again
        await asyncio.sleep(1)

if __name__ == "__main__":
    # Use asyncio to run the asynchronous scanning function
    asyncio.run(scan_specific_device("C4:A7:D8:EE:A2:39"))
