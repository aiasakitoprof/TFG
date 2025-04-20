package src.tg.coms;


import java.io.Serializable;


enum DataFrameType implements Serializable {
    APP_OBJECT,          // Pay load -> aplication object
    FRAME_REFUSED,      // Pay load -> null
    CONNECTION_REFUSED, // Pay load -> null
    COMMS_MESSAGE,      // Pay load -> message object
    STAY_ALIVE          // Pay load -> null
}
