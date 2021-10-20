package pison.soccergame.ai.pisonai;

import math.geom2d.Vector2D;

public enum PlayerPositions {
    GOALKEEPER, RIGHTBACK, LEFTBACK, RIGHTFORWARD, LEFTFORWARD;

    public Vector2D getTacticalPositionOfPlayer(int team, Tactics tactic) {
        switch (team) {
            case 0: {
                switch (tactic) {
                    case OFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(60, 260);
                            case RIGHTBACK:
                                return new Vector2D(400, 200);
                            case LEFTBACK:
                                return new Vector2D(400, 330);
                            case RIGHTFORWARD:
                                return new Vector2D(700, 160);
                            case LEFTFORWARD:
                                return new Vector2D(700, 350);
                        }
                    }
                    case HALFOFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(60, 260);
                            case RIGHTBACK:
                                return new Vector2D(200, 250);
                            case LEFTBACK:
                                return new Vector2D(450, 250);
                            case RIGHTFORWARD:
                                return new Vector2D(600, 160);
                            case LEFTFORWARD:
                                return new Vector2D(600, 360);
                        }
                    }
                    case DEFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(60, 260);
                            case RIGHTBACK:
                                return new Vector2D(200, 200);
                            case LEFTBACK:
                                return new Vector2D(200, 350);
                            case RIGHTFORWARD:
                                return new Vector2D(350, 100);
                            case LEFTFORWARD:
                                return new Vector2D(350, 450);
                        }
                    }
                    case HALFDEFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(60, 260);
                            case RIGHTBACK:
                                return new Vector2D(300, 200);
                            case LEFTBACK:
                                return new Vector2D(300, 330);
                            case RIGHTFORWARD:
                                return new Vector2D(500, 160);
                            case LEFTFORWARD:
                                return new Vector2D(500, 360);
                        }
                    }
                }
            }
            case 1: {
                switch (tactic) {
                    case OFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(860, 260);
                            case RIGHTBACK:
                                return new Vector2D(520, 200);
                            case LEFTBACK:
                                return new Vector2D(520, 330);
                            case RIGHTFORWARD:
                                return new Vector2D(220, 160);
                            case LEFTFORWARD:
                                return new Vector2D(220, 350);
                        }
                    }
                    case HALFOFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(860, 260);
                            case RIGHTBACK:
                                return new Vector2D(720, 250);
                            case LEFTBACK:
                                return new Vector2D(470, 250);
                            case RIGHTFORWARD:
                                return new Vector2D(320, 160);
                            case LEFTFORWARD:
                                return new Vector2D(320, 360);
                        }
                    }
                    case DEFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(860, 260);
                            case RIGHTBACK:
                                return new Vector2D(720, 200);
                            case LEFTBACK:
                                return new Vector2D(720, 350);
                            case RIGHTFORWARD:
                                return new Vector2D(570, 100);
                            case LEFTFORWARD:
                                return new Vector2D(570, 450);
                        }
                    }
                    case HALFDEFFENSIVE: {
                        switch (this) {
                            case GOALKEEPER:
                                return new Vector2D(860, 260);
                            case RIGHTBACK:
                                return new Vector2D(620, 200);
                            case LEFTBACK:
                                return new Vector2D(620, 330);
                            case RIGHTFORWARD:
                                return new Vector2D(410, 160);
                            case LEFTFORWARD:
                                return new Vector2D(410, 360);
                        }
                    }
                }
            }
            default: {
                throw new Error("Couldn't find the corresponding team.");

            }
        }
    }
}