package coopworld.game.Logs;

public class GameLanguage {
    Enums.Language language;
    Enums.LanguageDirection direction;

    public GameLanguage(Enums.Language language, Enums.LanguageDirection direction) {
        this.language = language;
        this.direction = direction;
    }

    public Enums.Language getLanguage() {
        return language;
    }

    public Enums.LanguageDirection getDirection() {
        return direction;
    }
}
