/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tg.local.vo;


import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum VOState implements Serializable {
    STARTING,
    ALIVE,
    PAUSED,
    COLLIDED,
    DEAD
}
