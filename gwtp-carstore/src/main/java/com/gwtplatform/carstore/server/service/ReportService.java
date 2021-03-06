/**
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.carstore.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gwtplatform.carstore.shared.dto.CarDto;
import com.gwtplatform.carstore.shared.dto.ManufacturerDto;
import com.gwtplatform.carstore.shared.dto.ManufacturerRatingDto;
import com.gwtplatform.carstore.shared.dto.RatingDto;

public class ReportService {
    public List<ManufacturerRatingDto> getAverageCarRatings(List<RatingDto> ratingDtos) {
        HashMap<String, AveragingCounter> averages = new HashMap<>();

        for (RatingDto ratingDto : ratingDtos) {
            CarDto carDto = ratingDto.getCar();
            ManufacturerDto manufacturer = carDto.getManufacturer();
            String manufacturerName = manufacturer.getName();
            Double rating = Double.valueOf(ratingDto.getRating());

            if (averages.containsKey(manufacturerName)) {
                averages.get(manufacturerName).add(rating);
            } else {
                averages.put(manufacturerName, new AveragingCounter(rating));
            }
        }

        List<ManufacturerRatingDto> results = new ArrayList<>(averages.size());
        for (String manufacturer : averages.keySet()) {
            results.add(new ManufacturerRatingDto(manufacturer, averages.get(manufacturer).average()));
        }

        return results;
    }

    private class AveragingCounter {
        private double sum;
        private int count;

        AveragingCounter(double number) {
            this.sum = number;
            this.count = 1;
        }

        void add(double number) {
            this.sum += number;
            this.count++;
        }

        double average() {
            return count == 0 ? Double.NaN : sum / count;
        }
    }
}
