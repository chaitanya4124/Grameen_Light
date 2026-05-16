# Project Plan

Add extra features to Grameen-Light:
1. Enhanced Map: High density of poles with lat/long and area names.
2. Authentication: User/Admin login with role-based access.
3. Localization: Support for Kannada and English.
4. Admin Dashboard: View all reports.
Build on the existing Navigation 3 and Room/Firebase architecture.

## Project Brief

# Grameen-Light Project Brief

Grameen-Light is a community-driven "Citizen-led Streetlight Audit" application. It enables villagers to monitor local infrastructure, reporting streetlight statuses (Working, Fused, or Burning in Day) to the Panchayat office to ensure "Zero-Dark" nights and promote energy conservation.

### Features
* **Enhanced Interactive Map:** A simulated map displaying a high density of streetlight poles with lat/long coordinates, area names (e.g., Market Square), and color-coded status indicators.
* **Role-Based Access Control:** Secure authentication for Users (to report and track) and Admins (to view and manage all reports across the village).
* **Localized Interface:** Full support for Kannada and English languages with a quick toggle to ensure accessibility for all community members.
* **Audit & Impact Dashboard:** A dual-purpose tracker to monitor the repair status of complaints and visualize community-wide "Energy Saved" metrics.

### High-Level Technical Stack
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose with Material Design 3 (Edge-to-Edge display).
* **Navigation:** **Jetpack Navigation 3** (state-driven approach).
* **Adaptive Strategy:** **Compose Material Adaptive** library for multi-pane and responsive layouts.
* **Backend & Sync:** Firebase (Authentication and Real-time Database for synchronization).
* **Persistence:** Room DB (for local caching of audit data).
* **Concurrency:** Kotlin Coroutines & Flow.

## Implementation Steps

### Task_1_DataLayer: Set up the Room database and Firebase Realtime Database for streetlight pole management and reporting.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Room entities (Pole, AuditReport) and DAO implemented
  - Firebase initialized and connected
  - Repository created for local/remote sync

### Task_2_NavigationUI: Implement Jetpack Navigation 3, Material 3 Theme with Light/Dark mode, and an Adaptive Layout shell.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Jetpack Navigation 3 structure established
  - Material 3 theme with dynamic color and toggle support
  - Adaptive layout shell using NavigationSuiteScaffold
  - Full Edge-to-Edge display enabled

### Task_3_CoreFeatures: Develop the Pole Audit Map, Quick Report functionality, Repair Status Tracker, and Energy Impact Dashboard.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Simulated map displays color-coded pole statuses
  - One-tap reporting updates Room and Firebase
  - Repair Tracker shows progress of complaints
  - Energy Impact Dashboard displays simulated savings

### Task_4_FinalizeVerify: Create an adaptive app icon and perform final verification of the application.
- **Status:** COMPLETED
- **Acceptance Criteria:**
  - Adaptive app icon created and integrated
  - Project builds successfully
  - App does not crash during manual testing
  - Navigation and data sync verified

### Task_5_AuthLocalization: Implement Firebase Authentication with role-based access (User/Admin) and add localization support for Kannada and English.
- **Status:** COMPLETED
- **Updates:** Implemented LoginScreen with User/Admin roles. Integrated Auth state with Navigation 3 backstack. Added full localization support for Kannada and English with a toggle. All UI strings moved to strings.xml for translation. Built and verified.
- **Acceptance Criteria:**
  - Authentication flow for User and Admin functional
  - Role-based access controls applied to Navigation 3
  - Localization toggle for Kannada and English working
  - All UI strings translated

### Task_6_MapAdminVerify: Enhance the Pole Map with high-density data (coordinates/area names), implement the Admin Dashboard, and perform a final Run and Verify.
- **Status:** IN_PROGRESS
- **Updates:** Coder implemented the Enhanced Map (30+ poles with locations), Admin Dashboard, and Localization. Critic verified the features but found localization gaps in dynamic status strings and a missing Logout button. Starting a refinement loop to address these feedback points.
- **Acceptance Criteria:**
  - Map displays poles with lat/long and area names
  - Admin Dashboard shows all reports for management
  - Project builds successfully and existing tests pass
  - App does not crash
  - Critic agent confirms alignment with requirements and stability
- **StartTime:** 2026-05-13 17:06:19 IST

