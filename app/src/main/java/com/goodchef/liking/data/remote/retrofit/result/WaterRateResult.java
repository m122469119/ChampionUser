package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;

import java.util.List;

/**
 * Created on 2017/6/27
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class WaterRateResult extends LikingResult {

    /**
     * data : {}
     */
    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data {
        /**
         * gym_name :
         * residue_water_time :
         * water_list : [{"water_id":2,"water_price":1,"water_time":1},{"water_id":1,"water_price":1,"water_time":""}]
         */

        private String gym_name;
        private String residue_water_time;
        private List<WaterListBean> water_list;


        public String getGym_name() {
            return gym_name;
        }

        public void setGym_name(String gym_name) {
            this.gym_name = gym_name;
        }

        public String getResidue_water_time() {
            return residue_water_time;
        }

        public void setResidue_water_time(String residue_water_time) {
            this.residue_water_time = residue_water_time;
        }

        public List<WaterListBean> getWater_list() {
            return water_list;
        }

        public void setWater_list(List<WaterListBean> water_list) {
            this.water_list = water_list;
        }


        public static class WaterListBean extends Data {


            /**
             * water_id : 1
             * water_price : 12.3
             * water_time : 10
             */

            private String water_id;
            private String water_price;
            private String water_time;
            private boolean checked;

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public String getWater_id() {
                return water_id;
            }

            public void setWater_id(String water_id) {
                this.water_id = water_id;
            }

            public String getWater_price() {
                return water_price;
            }

            public void setWater_price(String water_price) {
                this.water_price = water_price;
            }

            public String getWater_time() {
                return water_time;
            }

            public void setWater_time(String water_time) {
                this.water_time = water_time;
            }
        }
    }
}
