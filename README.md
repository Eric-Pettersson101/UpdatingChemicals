
## Project Overview

This repository contains code from a previous Java Maven project. The primary goal of this project was to automate chemical data entry for my role as an administrative assistant at MSU.

## Purpose

The code automates the following tasks:
1. Logging into the university website.
2. Reading data from an Excel spreadsheet.
3. Writing data to the database using Selenium.
4. Logging each entry to a file for record-keeping.

## Implementation

To handle some mismatches between the database and the Excel spreadsheet, as well as some quirks with the website's functionality, I used `SeleniumTest.java` to execute the code. 

### Key Components
- **Login Functionality:** Automates logging into the university website.
- **Data Reading:** Reads chemical data from an Excel spreadsheet.
- **Data Entry:** Writes the data to the university's database.
- **Logging:** Logs each data entry to a file for verification and record-keeping.
- **Verification Functions:** Ensures that the script correctly updates the data.

## Notes

- Some functions in the code are specifically designed to check if the script has correctly updated the data.
- There were challenges with mismatched data and website behavior, which required specific handling in `SeleniumTest.java`.

Feel free to review the code and provide any feedback or suggestions.
