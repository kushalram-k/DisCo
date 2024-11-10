# DisCo -Disaster Connect
  DisCo is a peer-to-peer messaging app designed for communication during emergencies when traditional networks are unavailable. The app establishes a Wi-Fi Direct P2P mesh network, enabling users to 
  communicate without internet or cellular services.

## Features
-  **User Authentication**: Users can register via OTP Authentication
- **Emergency Channels**: Provides users with quick access to predefined emergency channels (e.g., Food, Rescue, and others), allowing them to communicate with relevant groups based on their specific needs or 
    problems.
- **Private Messaging**: Allows users to chat directly with individuals connected to the mesh network, ensuring secure and private communication in emergencies.
- **Decentralized Database**: Stores user information, and message history on the device, allowing continued access even without internet connectivity.
- **Message History**: Stores a history of messages sent and received by the user, allowing them to refer back to previous communications.
- **Emergency Channel Management**: Displays all emergency channels the user has joined, showing important details and recent activity.
- **SOS Distress Signal**: Allows users to broadcast a distress message to nearby users, alerting them of the need for immediate assistance.

## TechStack
- **Java** for Frontend,Backend
- **SQLite** for Local Storage
- **FireBase** for User Authentication
- **WifiDirect** for Creating Mesh Network

## Requirements
    Android Studio,Android SDK,Android device(12+) 


## How to run this app?
1. Clone the Repositry: `git clone https://github.com/kushalram-k/DisCo.git`
2. Open the project in Android Studio.
3. It may take a while to build the project for the first time.
4. Once the build is over, run on the device using menu Run -> Run (app) (Launch app in emulator or phone)

## Permissions
1. Turn on WIFI
2. Enable Location Services
3. Allow Nearby Devices Permission (Android 12+)
    - Open `Settings`.
    - Go to `Apps > DisCo > Permissions`.
    - Tap on **Nearby Devices** and choose Allow to enable detection of nearby devices for peer-to-peer connections.

## How to Use
1. **Register**: Sign up using your email ID for authentication.
2. **Main Page**: After registration, you will be directed to the **Main Page**. From there, navigate to the **Private Page**.
3. **Discover Nearby Users**: Tap the **Discover** button to see a list of nearby users within the mesh network.
4. **Connect and Chat**: Click on a user to establish a connection. This will open a private chat where you can communicate with the selected user.
5. **Join Groups**: To communicate with multiple users, go to the **Groups Page** where you can join group chats and send messages to all participants.
6. **Alert Button**: Clicking on the **Alert** button (located at the bottom-right corner) will send an alert message to all nearby users, notifying them of your distress or important update.

## Contributors
  * [Hitesh](https://github.com/KazukiKenshi)
  * [Kushalram](https://github.com/kushalram-k)
  * [Avinash](https://github.com/yekkaladeviavinash)
  * [Lohith](https://github.com/lohith49)
  * [Sri Dattu](https://github.com/S2I-D4TT0)
  * [Leela Jogeendar sai](https://github.com/rljsai)


