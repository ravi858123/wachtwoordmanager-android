# 🔑 Wachtwoordmanager

Een eenvoudige, veilige en gratis wachtwoordmanager voor **Windows** en **Android**.
Alle wachtwoorden worden lokaal opgeslagen en versleuteld met AES-256 — niemand anders heeft toegang tot jouw gegevens.

![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![MIT License](https://img.shields.io/badge/Licentie-MIT-yellow?style=for-the-badge)
![AES-256](https://img.shields.io/badge/Versleuteling-AES--256-red?style=for-the-badge)
![APK app](https://www.google.com/imgres?q=apk%20icon&imgurl=https%3A%2F%2Fupload.wikimedia.org%2Fwikipedia%2Fcommons%2F4%2F41%2FAPK_format_icon_%25282014-2019%2529_%25282%2529.png&imgrefurl=https%3A%2F%2Fcommons.wikimedia.org%2Fwiki%2FFile%3AAPK_format_icon_(2014-2019)_(2).png&docid=aSm6bOacML0EZM&tbnid=GgB8pmaijYSMgM&vet=12ahUKEwjo4cyAgKCVAxVA9gIHHSThAOoQnPAOegQINhAA..i&w=1024&h=1024&hcb=2&ved=2ahUKEwjo4cyAgKCVAxVA9gIHHSThAOoQnPAOegQINhAA)

---

## 📸 Screenshots

> *Windows en Android versie — binnenkort beschikbaar*

---

##  Functies

-  Beveiligd met een hoofdwachtwoord (AES-256 versleuteling)
-  Sla websites, gebruikersnamen, wachtwoorden en notities op
-  Zoek door je opgeslagen items
-  Kopieer wachtwoord met één klik naar het klembord
-  Ingebouwde wachtwoordgenerator (instelbare lengte en symbolen)
-  Alle data wordt lokaal opgeslagen — niets gaat naar het internet

---

## 📦 Installatie

### Windows

1. Download `Wachtwoordmanager.exe` uit de [laatste release](../../releases/latest)
2. Dubbelklik op het bestand en start de app
3. Geen installatie nodig

### Android

1. Download `Wachtwoordmanager.apk` uit de [laatste release](../../releases/latest)
2. Open het bestand op je telefoon
3. Ga naar **Instellingen → Beveiliging** en schakel **Onbekende bronnen** in als daarom gevraagd wordt
4. Installeer de app

---

## 🔐 Beveiliging

| Onderdeel | Details |
|---|---|
| Versleuteling | AES-256-GCM |
| Sleutelafleiding | PBKDF2 met SHA-256, 480.000 iteraties |
| Opslag | Lokaal op je apparaat |
| Netwerk | Geen — de app maakt geen internetverbinding |

> ⚠️ Er is geen "wachtwoord vergeten" optie. Onthoud je hoofdwachtwoord goed — zonder dit zijn je gegevens niet toegankelijk.

---

## 🛠️ Zelf bouwen

### Windows EXE

Vereisten: [Python 3.8+](https://www.python.org/downloads/)

```bash
pip install cryptography pyinstaller
pyinstaller --onefile --windowed --name "Wachtwoordmanager" --icon "wachtwoordmanager.ico" wachtwoordmanager.py
```

Of dubbelklik op `bouw_exe.bat`.

### Android APK

De APK wordt automatisch gebouwd via GitHub Actions bij elke push naar `main`.
Download de APK onder het tabblad **Actions → Artifacts**.

---

## 📁 Projectstructuur

```
wachtwoordmanager/
├── wachtwoordmanager.py              # Windows applicatie (Python/Tkinter)
├── wachtwoordmanager.ico             # Windows icoon
├── bouw_exe.bat                      # Windows build script
├── android/                          # Android applicatie (Java)
│   ├── app/src/main/
│   │   ├── java/nl/wachtwoordmanager/
│   │   │   ├── App.java
│   │   │   ├── Crypto.java           # AES-256 versleuteling
│   │   │   ├── Kluis.java            # Data opslag
│   │   │   ├── LoginActivity.java
│   │   │   ├── MainActivity.java
│   │   │   ├── ItemActivity.java
│   │   │   ├── ItemAdapter.java
│   │   │   └── WachtwoordItem.java
│   │   └── res/                      # Layouts, iconen, teksten
│   └── .github/workflows/
│       └── bouw_apk.yml              # Automatische APK build
└── LICENSE
```

---

## 🤝 Bijdragen

Bijdragen zijn welkom! Voel je vrij om een [issue](../../issues) aan te maken of een [pull request](../../pulls) in te dienen.

---

## 📄 Licentie

Dit project valt onder de [MIT-licentie](LICENSE).
