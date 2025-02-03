# theLost Project

## Overview

The **theLost** project is a system designed to locate specific Bluetooth Low Energy (BLE) devices and track their proximity using RSSI (Received Signal Strength Indicator). The project integrates with Firebase to store and analyze location data in real-time. It was supposed to communicate with the app, but as a team, we failed to finish it in time during the hackathon.

## Features

- **BLE Scanning**: Uses `bleak` to scan for BLE devices.
- **Real-time Database**: Pushes scanned device data to Firebase.
- **Distance Calculation**: Estimates the distance to a BLE device based on RSSI.
- **Automated Alerts**: Sends alerts when a device is within a predefined distance range.

## Installation & Setup

### Prerequisites

- Python 3.8+
- pip (Python package manager)
- Firebase account with Realtime Database enabled

### Installation

1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd theLost
   ```
2. Install required dependencies:
   ```sh
   pip install bleak pyrebase asyncio fnmatch
   ```

## Usage

### BLE Scanning & Firebase Integration

1. Ensure your Firebase Realtime Database is set up with a valid **databaseURL**.
2. Modify `talkWithMicro.py` to use the correct **MAC address** of the BLE device.
3. Run the BLE scanner:
   ```sh
   python talkWithMicro.py
   ```
   - The script scans for a BLE device and pushes distance-based status updates to Firebase.

## Code Structure

### Main Components

- **talkWithMicro.py** – Scans for BLE devices and pushes data to Firebase.
- **.gitattributes.txt** – Defines Git attributes for handling text and binary files.
- **.gitignore** – Specifies files and directories to exclude from version control.
- **README.md** – Project documentation.

## Future Improvements

- Implement a web dashboard for real-time tracking.
- Add support for multiple BLE devices.
- Improve error handling and logging.
- Extend compatibility beyond Windows systems.

## License

This project is licensed under [Your License Here].
