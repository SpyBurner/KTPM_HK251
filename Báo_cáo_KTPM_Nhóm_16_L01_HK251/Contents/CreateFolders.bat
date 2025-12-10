@echo off
setlocal enabledelayedexpansion

:: -----------------------------------------------------------------------------
:: Batch Script to Create Folders from .tex File Names
:: -----------------------------------------------------------------------------
:: This script iterates through all files ending in .tex in the current directory
:: and creates a new directory for each one, using the file name without the
:: extension as the new folder name.
:: -----------------------------------------------------------------------------

echo.
echo Starting folder creation process...
echo.

:: Loop through every file in the current directory that ends with .tex
for %%f in (*.tex) do (
    :: Extract the file name without the extension (the base name)
    set "FOLDER_NAME=%%~nf"

    :: Check if the folder already exists before trying to create it
    if not exist "!FOLDER_NAME!\" (
        :: Create the new folder
        mkdir "!FOLDER_NAME!"
        echo Created folder: "!FOLDER_NAME!"
    ) else (
        echo Folder already exists: "!FOLDER_NAME!" (Skipping)
    )
)

echo.
echo Folder creation complete.
endlocal
pause