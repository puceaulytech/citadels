package com.github.the10xdevs.citadels.districtImporter;

import com.github.the10xdevs.citadels.models.District;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DistrictImporter {
    public List<District> importFromJson(String filePath) throws IOException {
        // Read the file to a String
        String json = new String(Files.readAllBytes(Paths.get(filePath)));

        // Define the type for the Gson converter
        Type districtListType = new TypeToken<List<District>>() {
        }.getType();

        // Convert the JSON String to a List of Districts
        List<District> districts = new Gson().fromJson(json, districtListType);

        return districts;
    }
}