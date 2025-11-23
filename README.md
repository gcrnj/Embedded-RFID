# Project Setup

This project requires two values inside your `local.properties` file:

```
SHEETS_CONTENT_KEY=<your-content-key>
SHEETS_LIB=<your-library-id>
```

## How to Get These Values from Google Sheets

Follow the steps below to obtain the required keys:

### 1. **SHEETS_CONTENT_KEY**

This is the **Spreadsheet ID** from your Google Sheet.

**Steps to get it:**

1. Open your Google Sheet.
2. Look at the URL. It will look like:

   ```
   https://docs.google.com/spreadsheets/d/1AbCdEfGhIjKlMnOpQrStUvWxYz1234567890/edit#gid=0
   ```
3. Copy the long string between `/d/` and `/edit`:

   ```
   1AbCdEfGhIjKlMnOpQrStUvWxYz1234567890
   ```
4. Paste it into `local.properties` as:

   ```
   SHEETS_CONTENT_KEY=1AbCdEfGhIjKlMnOpQrStUvWxYz1234567890
   ```

---

### 2. **SHEETS_LIB**

This is the **Apps Script Deployment ID** from your Google Apps Script project.

**Steps to get it:**

1. Open your Google Sheet.
2. Go to **Extensions → Apps Script**.
3. In the Apps Script editor, open the left menu and click **Deploy → Manage Deployments**.
4. Select your deployment.
5. Copy the **Deployment ID** shown.
6. Paste it into `local.properties` as:

   ```
   SHEETS_LIB=your-deployment-id
   ```

---

## 📥 Example `local.properties`

```
SHEETS_CONTENT_KEY=1AbCdEfGhIjKlMnOpQrStUvWxYz1234567890
SHEETS_LIB=AKfycbxYourDeploymentIdHere123456
```

You're all set! Make sure **local.properties is NOT committed** to version control.
