# 🌙 SleepMoon - Sleep Tracking Application

<img width="599" height="798" alt="Ekran Resmi 2025-12-24 15 03 39" src="https://github.com/user-attachments/assets/5b462bf6-82f1-4060-a8e6-2d645d1a006e" />

<img width="597" height="847" alt="Ekran Resmi 2025-12-24 15 03 50" src="https://github.com/user-attachments/assets/ed8d8759-eaba-461e-ba3e-adb0a3800ce7" />

<img width="597" height="847" alt="Ekran Resmi 2025-12-24 15 03 57" src="https://github.com/user-attachments/assets/8d67fedf-a866-4cd6-a08b-db1a39808261" />

<img width="597" height="847" alt="Ekran Resmi 2025-12-24 15 04 04" src="https://github.com/user-attachments/assets/ff61c004-ba4a-4cfa-931d-0a8cfbf5f4c8" />

<img width="597" height="847" alt="Ekran Resmi 2025-12-24 15 04 19" src="https://github.com/user-attachments/assets/326ea55e-4701-4918-85a2-c5fa3984908a" />

<img width="597" height="847" alt="Ekran Resmi 2025-12-24 15 04 31" src="https://github.com/user-attachments/assets/a172598b-3196-42a7-b422-78487c74c235" />

<img width="597" height="847" alt="Ekran Resmi 2025-12-24 15 04 45" src="https://github.com/user-attachments/assets/88e857f1-46ff-45ab-8dab-f90918d23f13" />

<img width="597" height="833" alt="Ekran Resmi 2025-12-24 15 05 00" src="https://github.com/user-attachments/assets/8f7432e8-553a-48fd-b5a0-e455eb4fd73c" />



## 📖 About

**SleepMoon** is a sleep tracking application that helps users monitor and improve their sleep quality. With an dark UI design featuring soothing purple-pink gradients, SleepMoon makes sleep tracking a pleasant daily habit.

-------

## ✨ Features

### 📊 Sleep Tracking
- **Entry System**: Easy-to-use spinners for bed time and wake time
- **Automatic Duration Calculation**: Calculates total sleep hours automatically
- **Quality Rating System**: 1-5 star rating system with visual feedback
- **Mood Tracking**: Track your morning mood with emoji indicators
- **Personal Notes**: Add notes about your sleep experience

### ⏰ Smart Alarms
- **Multiple Alarms**: Set unlimited alarms
- **Day Scheduling**: Choose specific days for each alarm
- **Custom Sounds**: 5 alarm sound options with preview
- **Snooze Function**: 5-minute snooze feature
- **Easy Toggle**: Quick on/off switch for each alarm

### 🎵 White Noise Player
- **12 Ambient Sounds**:
  - Nature: Rain, Ocean, Forest, Thunderstorm
  - Fire: Fireplace
  - Noise: White Noise, Brown Noise
  - Urban: City, Cafe, Train
  - Music: Piano, Guitar
- **Volume Control**: Adjustable volume slider
- **Sleep Timer**: Auto-stop options (15min, 30min, 1hr, 2hrs)
- **Playback Controls**: Play, pause, and stop functionality

### 📈 Statistics & Analytics
- **Sleep Trends**: Visualize sleep duration over time with line charts
- **Quality Analysis**: Track sleep quality with bar charts
- **Mood Correlation**: Analyze how mood affects sleep
- **Best/Worst Nights**: Identify your best and worst sleep sessions
- **Custom Date Ranges**: View stats for 7, 14, 30 days, or all time
- **Average Calculations**: See average sleep duration and quality

### 🎨 UI Design
- **Glassmorphism Aesthetic**: Modern, elegant card-based design
- **Purple-Pink Gradient Theme**: Soothing color palette perfect for sleep apps
- **Smooth Animations**: Polished transitions and effects
- **Dark Mode Optimized**: Easy on the eyes, especially at night
- **Responsive Layout**: Clean, organized interface

------

## 🚀 Installation

### Prerequisites
- **JDK 21** or higher
- **Maven** (for building)
- **JavaFX 21**

### Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/sleep-moon.git
cd sleep-moon
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn javafx:run
```

---

## 💻 Usage

### First Launch
1. When you first open Sleep Moon, you'll see a welcome screen
2. Enter your name to personalize your experience
3. Click "Continue" to access the main application

### Adding a Sleep Entry
1. Select the date from the date picker
2. Set your bed time using the hour and minute spinners
3. Set your wake time
4. Rate your sleep quality (1-5 stars)
5. Choose your morning mood
6. Add optional notes
7. Click "💾 Save Entry"

### Setting an Alarm
1. Click the "⏰" button in the header
2. Set the alarm time
3. Give your alarm a name (optional)
4. Choose an alarm sound
5. Select repeat days
6. Click "💾 Save Alarm"

### Playing White Noise
1. Click the "🎵" button in the header
2. Select a sound from the grid
3. Adjust volume as needed
4. Set a sleep timer (optional)
5. Click play/pause to control playback

### Viewing Statistics
1. Click the "📊" button in the header
2. Select a time period from the dropdown
3. View your sleep trends, quality charts, and mood analysis
4. Identify patterns in your sleep data

## 🛠️ Tech Stack

### Core Technologies
- **Java 21**: Modern Java features and performance
- **JavaFX 21**: Rich desktop UI framework
- **Maven**: Dependency management and build automation

### Libraries & Dependencies
- **SQLite JDBC (3.44.1.0)**: Lightweight database for data persistence
- **JLayer (1.0.1)**: MP3 audio playback
- **Gson (2.10.1)**: JSON serialization/deserialization

### Design
- **Custom CSS**: Glassmorphism effects and gradient styling
- **JavaFX Charts**: Line charts and bar charts for data visualization

---

## 📁 Project Structure

SleepTracker/
src/main/
java/org/sleeptracker/
Main.java
controllers/
- MainController.java
- AlarmController.java
- StatsController.java
- NoiseController.java
- WelcomeController.java
models/
- SleepEntry.java
- Alarm.java
- MoodType.java
database/
- DatabaseManager.java
services/
- AlarmService.java
- AudioPlayer.java
resources/org/sleeptracker/
fxml/
- main.fxml
- alarm.fxml
- stats.fxml
- noise.fxml
- welcome.fxml
css/
- style.css
media/
- [audio files]
pom.xml
README.md
-----

## 🔮 Future Enhancement ideas

- [ ] Sleep quality prediction using machine learning
- [ ] Export data to CSV/PDF
- [ ] Cloud sync with user accounts
- [ ] Mobile app (iOS/Android)
- [ ] Integration with fitness trackers
- [ ] Social features for sleep challenges
- [ ] Customizable themes
- [ ] Multilingual support

---------

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch 
3. Commit your changes 
4. Push to the branch 
5. Open a Pull Request

## 👤 Author

**Yasemin Adatepe**

- LinkedIn:www.linkedin.com/in/yasemin-adatepe-a25a4922b


## 🙏 Acknowledgments

- Icons and emojis from Unicode
- Audio samples from Youtube
- Inspiration from modern sleep tracking apps
- Design help is from ClaudeAI
