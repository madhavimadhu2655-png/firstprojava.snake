# 🐍 Multiplayer Snake Game

A real-time multiplayer snake game built with **Spring Boot + WebSockets** and **HTML5 Canvas**.

## 🚀 Run Locally

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps
```bash
mvn spring-boot:run
```
Open **http://localhost:8080** in multiple browser tabs to test multiplayer!

---

## ☁️ Deploy to Railway (Free Public URL)

1. **Push to GitHub:**
```bash
git init
git add .
git commit -m "Real-time multiplayer snake game"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/snake-game.git
git push -u origin main
```

2. Go to **https://railway.app** → Sign in with GitHub

3. Click **New Project** → **Deploy from GitHub repo** → select `snake-game`

4. Railway auto-detects the Dockerfile and builds it (~2 min)

5. Click **Settings → Networking → Generate Domain** to get your public URL like:
   `https://snake-game-production.up.railway.app`

6. **Share the link** — anyone can play instantly in their browser! 🌍

---

## 🎮 How to Play
- Enter your name and click **PLAY**
- Use **Arrow Keys** or **WASD** to move
- Eat the 🔴 red food to grow and score points
- Avoid walls, yourself, and other players
- If you die, click **RESPAWN** to jump back in

## 🏗️ Tech Stack
| Layer | Technology |
|-------|-----------|
| Backend | Java 17 + Spring Boot 3 |
| Real-time | WebSockets (STOMP-free, raw WS) |
| Frontend | HTML5 Canvas + Vanilla JS |
| Deployment | Docker + Railway.app |
