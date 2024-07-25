package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // Class-level variable to store the IP address
    private String targetIp;
    private int port = 5000; // Port number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final TextInputEditText targetIpEditText = findViewById(R.id.textInputEditText);
        final Button goButton = findViewById(R.id.button4);

        // Set a click listener for the button
        goButton.setOnClickListener(v -> {
            // Get the entered IP address
            targetIp = Objects.requireNonNull(targetIpEditText.getText()).toString().trim();

            // Perform action based on the target IP address
            Toast.makeText(MainActivity.this, "Target IP: " + targetIp, Toast.LENGTH_SHORT).show();
        });
    }

    // Method to use the stored IP address
    private void performAction(String command) {
        if (targetIp != null && !targetIp.isEmpty()) {
            // Execute network operations in a separate thread
            new Thread(() -> {
                try (
                        Socket socket = new Socket(targetIp, port); // Initialize socket
                        OutputStream output = socket.getOutputStream(); // Initialize output stream
                        PrintWriter writer = new PrintWriter(output, true) // Initialize PrintWriter
                ) {
                    // Send the command to the server
                    writer.println(command);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle exceptions appropriately
                }
            }).start();
        } else {
            Toast.makeText(this, "No IP address provided", Toast.LENGTH_SHORT).show();
        }
    }

    // Example of calling performAction from another method
    public void lockSys(View view) {
        Toast.makeText(this, "Locking Your System....", Toast.LENGTH_SHORT).show();
        performAction("lock"); // Call your method with the stored IP address
    }

    public void cameraSys(View view) {
        Toast.makeText(this, "Opening Camera....", Toast.LENGTH_SHORT).show();
        performAction("camera"); // Call your method with the stored IP address
    }

    public void micSys(View view) {
        Toast.makeText(this, "Opening Mic....", Toast.LENGTH_SHORT).show();
        performAction("mic"); // Call your method with the stored IP address
    }
}
