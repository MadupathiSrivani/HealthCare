<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patient Details Entry</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            min-height: 100vh;
            background: #f7f7f7;
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
        }
        header {
            width: 100%;
            text-align: center;
            padding: 30px 0 10px 0;
            background: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            font-size: 2em;
            font-weight: bold;
            letter-spacing: 1px;
        }
        .center-wrapper {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container {
            background: #fff;
            padding: 32px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.09);
            width: 100%;
            max-width: 400px;
        }
        label {
            font-weight: 500;
        }
        input[type="text"], input[type="date"], select {
            width: 100%;
            padding: 8px;
            margin-top: 4px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1em;
            box-sizing: border-box;
        }
        input[type="submit"] {
            background: #111;
            color: #fff;
            border: none;
            padding: 12px 32px;
            border-radius: 4px;
            font-size: 1em;
            cursor: pointer;
            transition: background 0.2s;
        }
        input[type="submit"]:hover {
            background: #333;
        }
        #admitted_row {
            margin-bottom: 16px;
        }
    </style>
    <script>
        function toggleAdmittedDate() {
            var patientType = document.getElementById('patient_type').value;
            var admittedRow = document.getElementById('admitted_row');
            if (patientType === 'IP') {
                admittedRow.style.display = '';
            } else {
                admittedRow.style.display = 'none';
                document.getElementById('admitted_date').value = '';
            }
        }
    </script>
</head>
<body>
    <header>
        Enter Patient Details
    </header>
    <div class="center-wrapper">
        <div class="container">
            <form action="addAppointment" method="post">
                <label>Patient Type:</label>
                <select name="patient_type" id="patient_type" onchange="toggleAdmittedDate()" required>
                    <option value="">--Select--</option>
                    <option value="IP">IP</option>
                    <option value="OP">OP</option>
                </select>

                <label>DOB:</label>
                <input type="date" name="dob" required>

                <label>Disease:</label>
                <input type="text" name="disease" required>

                <label>Previous History:</label>
                <select name="previous_history" required>
                    <option value="">--Select--</option>
                    <option value="cold">Cold</option>
                    <option value="cough">Cough</option>
                    <option value="fever">Fever</option>
                    <option value="Heart disease">Heart disease</option>
                    <option value="Kidney issues">Kidney issues</option>
                </select>

                <label>Doctor Type:</label>
                <select name="doctor_type" required>
                    <option value="">--Select--</option>
                    <option value="General">General</option>
                    <option value="Cardiologist">Cardiologist</option>
                    <option value="Nephrologist">Nephrologist</option>
                    <option value="Pediatrician">Pediatrician</option>
                </select>

                <div id="admitted_row" style="display:none;">
                    <label>Admitted Date:</label>
                    <input type="date" name="admitted_date" id="admitted_date">
                </div>

                <input type="submit" value="Add Details">
            </form>
        </div>
    </div>
</body>
</html>
