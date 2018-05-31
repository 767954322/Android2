package com.tuwan.yuewan.nim.uikit.contact.core.model;

import java.util.List;

public interface IContact {

    interface Type {

        /**
         * TYPE USER
         */
        int Friend = 0x1;

        /**
         * TYPE TEAM
         */
        int Team = 0x2;

        /**
         * TYPE TEAM MEMBER
         */
        int TeamMember = 0x03;

        /**
         * TYPE_MSG
         */
        int Msg = 0x04;
    }

    /**
     * get contact id
     *
     * @return
     */
    String getContactId();

    /**
     * get contact type {@link Type}
     *
     * @return
     */
    int getContactType();

    /**
     * get contact's display name to show to user
     *
     * @return
     */
    String getDisplayName();


    String getAvart();

    int getAge();

    int getGender();

    List<String> getIcons();

}
