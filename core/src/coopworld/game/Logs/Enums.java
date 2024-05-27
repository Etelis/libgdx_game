package coopworld.game.Logs;

/**
 * Created by User on 22/02/2017.
 */
public class Enums {

    public enum Player
    {
        Human,
        Virtual
    }

    public enum GenerationCoinsPolicy
    {
        WhenNoCoins,
        WhenPossible
    }

    public enum GenerationIceCubesPolicy
    {
        WhenNoIceCubes,
        WhenPossible
    }

    public enum Gender
    {
        Male,
        Female
    }

    public enum LevelDescription
    {
        // No obstacles.
        OnlyCoins,
        CoinsAndIceCubes,
        // Green obstacles only.
        HumanRequests,
        // Ice obstacles only.
        VirtualRequests,
        // Combine two obstacles.
        BothRequests
    }

    public enum ObstacleType
    {
        Ice,
        Nature
    }

    public enum VirtualPlayerStrategy{
        ALLC,
        ALLD,
        Delay,
        TFT,
        RANDOM,
        NoResponse,
        Alternate,
        ProbabilisticTFT
    }

    public enum Language{
        Hebrew,
        English,
        Arabic,
        Macedonian
    }

    public enum LanguageDirection{
        RTL,
        LTR
    }

    public enum HelpLogic{
        Walk,
        Pay
    }

    public enum GameSetting{
        FreePlay,
        Experimental
    }

    public enum GameContinuity{
        OneTime,
        Continual
    }

    public enum UserStatus{
        New,
        Exists
    }

    public enum TimingRequestLogic{
        // stand next to the nature obstacle until x seconds pass.
        StandNextToObstacle,
        // continue playing (collecting ice cubes) until x seconds pass.
        ContinuePlaying
    }

    public enum InstructionsMethod{
        // only one level for instructions - which consists of all the elements required for
        // learning the game.
        Combined,
        // 4 levels for learning. each level will explain different element.
        Extended
    }

    public enum Group
    {
        Experimental,
        Control
    }

    public enum FormScreenState{
        In_Progress,
        Next_Clicked,
        Indication_Is_On,
        End
    }

    public enum LevelType{
        REGULAR,
        ICE,
        BEACH,
        DESERT,
        CANDIES
    }

    public enum ChosenStrategy{
        PREDEFINED_HELP,
        TFT,
        REVERSED_TFT
    }

    public enum EndReason
    {
        GAME_END,
        SESSION_END
    }
}
