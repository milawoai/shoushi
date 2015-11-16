package com.learn.shoushi.userManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class FakeInfo {
    public static List<RelativeMemberData> fakeMemeberData;

    static {
        fakeMemeberData = new ArrayList<RelativeMemberData>() {
            {
                fakeMemeberData.add(new RelativeMemberData("周",
                        Arrays.asList(new String[]{"1", "12", "13"}),
                        Arrays.asList(new String[]{"2", "23", "24"}),
                        Arrays.asList(new String[]{"2", "23", "24"}), 0, 2));
                fakeMemeberData.add (new RelativeMemberData("周",
                                Arrays.asList(new String[]{"1", "12", "13"}),
                                Arrays.asList(new String[]{"2", "23", "24"}),
                                Arrays.asList(new String[]{"2", "23", "24"}), 0, 2));
                fakeMemeberData.add( new RelativeMemberData("周",
                                Arrays.asList(new String[]{"1", "12", "13"}),
                                Arrays.asList(new String[]{"2", "23", "24"}),
                                Arrays.asList(new String[]{"2", "23", "24"}), 0, 2));
            }

        };
    }
}
