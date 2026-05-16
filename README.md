Product Requirements Document
Grameen-Light: Citizen-led Streetlight Audit System
1. Product Vision & Overview
Grameen-Light is a "Citizen-led Streetlight Audit" mobile application designed to act as a digital
bridge between village residents and their local Panchayat office. By empowering citizens to
report the status of local lamp posts, the app aims to eliminate energy waste and ensure "Zero
Dark" nights. It serves as a practical application of smart village concepts, utilizing community
participation to achieve real-time monitoring and maintenance.
2. Problem Statement
Currently, rural street lighting systems suffer from inefficiencies due to a lack of real-time
monitoring. Streetlights often remain 
ON during daylight hours due to manual errors, leading
to significant electricity waste. Conversely, fused bulbs can leave streets in darkness for weeks
because the local Panchayat is unaware of the malfunction, directly compromising public safety
and security.
3. Target Audience
• 
• 
Primary Users (Reporters): Village residents who observe and report the status of
streetlights during their daily routines.
Secondary Users (Resolvers): Panchayat members or local electrical maintenance
workers who track, assign, and resolve the generated complaints.
4. Core Features & User Flow
• 
• 
• 
• 
Interactive Pole Map: A map interface displaying all electrical poles within the village.
Poles are visually represented with color-coded dots indicating their current operational
status.
One-Tap Quick Report: A highly simplified UI allowing users to tap a specific pole on the
map and instantly report its status (e.g., Working, Fused, Burning in Day). The system auto
generates a unique "Complaint ID" for every submitted report.
Repair Tracker: A transparent dashboard where users can monitor the lifecycle of their
reports (Submitted → Assigned → Fixed).
Energy Goal Dashboard: A visual analytics section displaying "Energy Saved this month,"
calculating the community impact of daytime reporting and timely switch-offs.
5. Technical Specifications & Architecture
• 
• 
• 
• 
Frontend Development: Native Android development using Kotlin and the Jetpack
Compose framework to build the simplified, one-tap reporting UI and efficiently manage the
state of the interactive map.
Theming Context: A built-in "Dark/Light" theme switch. This serves as a UI preference and
symbolically represents night/day audit modes within the app context.
Backend & Synchronization: Firebase Realtime Database to handle data syncing,
ensuring that when a pole status is updated by one resident, it reflects instantly across the
village network.
Local Persistence: Room Database for caching local records, ensuring the app remains
partially functional and retains user history even with intermittent internet connectivity in
rural areas.
6. Impact Goals
• 
• 
• 
Smart Villages: Translating standard IoT monitoring concepts into a manual, community
driven application accessible to everyone.
Energy Efficiency: Drastically reducing power wastage at the local government level
through rapid reporting of day-burning lights.
Public Safety: Ensuring properly lit streets so that vulnerable demographics, including
women and children, can navigate safely at night.
7. Success Criteria
• 
• 
• 
UI/UX Frictionless Design: The reporting process requires only a single tap to log a status
from the main map view.
Functional Accuracy: The map accurately renders updated color-coded statuses for every
simulated pole in real-time.
System Reliability: 100% of submitted reports successfully generate a tracked Complaint
ID and synchronize seamlessly to Firebase.
