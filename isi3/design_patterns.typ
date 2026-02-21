#import "../template/polytech.typ": *;

#let principia(..content) = grid(
  columns: (1fr, 1fr),
  column-gutter: 20pt,
  ..content.pos(),
)

#show: conf(doctitle: "Design Patterns", subject: "ISI3", theme: rgb("#079452"))[
  #titlepage(
    authors: "Clément RENIERS",
  )

  = Stratégie

  === Principe

  #principia(
    [
      => On dépend des classes abstraites, rajouter une stratégie ne change rien au code.

      => Utilisable quand on a plusieurs variantes d'un algorithme.

      => Au contraire du pattern State, on change de stratégie de manière explicite. Les stratégies ne sont pas liées à un état de l'application.
    ],
    rounded-image(image("/assets/image-1.png"), caption: "Utilisation simple du pattern Stratégie"),
  )

  === Exemple (Tri)

  #show raw: set text(size: 0.7em)

  ```java
  public interface SortingStrategy {
      void sort(int[] array);
  }

  public class SortingContext {
      private SortingStrategy sortingStrategy;

      public SortingContext(SortingStrategy sortingStrategy) {
          this.sortingStrategy = sortingStrategy;
      }

      public void setSortingStrategy(SortingStrategy sortingStrategy) {
          this.sortingStrategy = sortingStrategy;
      }

      public void performSort(int[] array) {
          sortingStrategy.sort(array);
      }
  }
  ```

  ```java
  public interface SortingStrategy {
      void sort(int[] array);
      // Implement sorting logic here
  }
  ```

  ```java
  // BubbleSortStrategy
  public class BubbleSortStrategy implements SortingStrategy {
      @Override
      public void sort(int[] array) {
          // Implement Bubble Sort algorithm
          System.out.println("Sorting using Bubble Sort");
      }
  }

  // MergeSortStrategy
  public class MergeSortStrategy implements SortingStrategy {
      @Override
      public void sort(int[] array) {
          // Implement Merge Sort algorithm
          System.out.println("Sorting using Merge Sort");
      }
  }

  // QuickSortStrategy
  public class QuickSortStrategy implements SortingStrategy {
      @Override
      public void sort(int[] array) {
          // Implement Quick Sort algorithm
          System.out.println("Sorting using Quick Sort");
      }
  }
  ```





  = État

  === Principe

  #principia(
    [
      => Sert dans le cas d'une classe qui doit avoir un comportement différent selon les états dans lesquels elle se trouve.

      => Permet d'éviter les gros blocs conditionnels

      => C'est l'état qui se charge de réaliser les actions.

      => Différence avec #link("#Stratégie", "Stratégie") : le comportement peut-être changé dans la durée de vie de la clase
    ],
    rounded-image(image("/assets/image-2.png"), caption: "Utilisation simple du pattern État"),
  )

  === Exemple (Lecteur audio)
  => On imagine un lecteur audio avec un unique bouton pour play/pause.

  => On modélise deux états : "Playing" et "Paused". Le comportement du bouton change selon l'état dans lequel on se trouve.

  ```java
  public class Reader {
      private ReaderState _state = null;

      public Reader(ReaderState state) {
           this._state = state;
      }

      public void PressPlay() {
          this._state.PressPlay(this);
      }

      public ReaderState CurrentState {
          get { return _state; }
          set { _state = value; }
      }
  }
  ```

  ```java
    public abstract class ReaderState {
        public abstract void PressPlay(Reader reader);
    }

    public class ReaderPlayingState extends ReaderState {
        public ReaderPlayingState() {
            System.out.println("Reader playing");
        }

        @Override
        public void PressPlay(Reader reader) {
            reader.CurrentState = new ReaderPausedState();
        }
    }

    public class ReaderPausedState extends ReaderState {
        public ReaderPausedState() {
            System.out.println("Reader paused");
        }

        @Override
        public void PressPlay(Reader reader) {
            reader.CurrentState = new ReaderPlayingState();
        }
    }
  ```


  = Observateur

  === Principe

  #principia(
    [
      => Permet à un sujet de notifier ses observateurs lorsqu'il change d'état. Les observateurs peuvent alors réagir à ce changement.

      => Le sujet ne connaît pas les observateurs, il leur envoie juste une notification.

      => Utilisé dans les interfaces graphiques, les systèmes d'événements, etc.
    ],
    rounded-image(image("/assets/image-3.png"), caption: "Utilisation simple du pattern Observateur"),
  )

  === Exemple (Chaîne YouTube)
  => On modélise une chaîne YouTube suivie par des abonnés. Lorsqu'une nouvelle vidéo est publiée, la chaîne notifie ses abonnés.

  ```java
  public interface YouTubeChannel {
      void addSubscriber(Subscriber subscriber);
      void removeSubscriber(Subscriber subscriber);
      void notifySubscribers();
  }
  ```

  #grid(
    columns: (1fr, 1fr),
    column-gutter: 40pt,
    ```java
    public class YouTubeChannelImpl implements YouTubeChannel {
        private List<Subscriber> subscribers = new ArrayList<>();
        private String video;

        @Override
        public void addSubscriber(Subscriber subscriber) {
            subscribers.add(subscriber);
        }

        @Override
        public void removeSubscriber(Subscriber subscriber) {
            subscribers.remove(subscriber);
        }

        @Override
        public void notifySubscribers() {
            for (Subscriber subscriber : subscribers) {
                subscriber.update(video);
            }
        }

        public void uploadNewVideo(String video) {
            this.video = video;
            notifySubscribers();
        }
    }
    ```,
    ```java
      public interface Subscriber {
          void update(String video);
      }

      public class EmailSubscriber implements Subscriber {
          private String email;
          public EmailSubscriber(String email) {
              this.email = email;
          }

          @Override
          public void update(String video) {
              System.out.println("Sending email to " + email + ": New video uploaded: " + video);
          }
      }
    ```,
  )

  = Singleton

  === Principe

  #principia(
    [
      => Permet de s'assurer qu'une classe n'a qu'une seule instance et de fournir un point d'accès global à cette instance.

      => Utilisé pour les ressources partagées, les gestionnaires de configuration, etc.
    ],
    rounded-image(image("/assets/image-4.png"), caption: "Utilisation simple du pattern Singleton"),
  )

  === Exemple

  ```java
  public final class Singleton {
      private static Singleton instance;
      public String value;

      // Constructeur privé
      private Singleton(String value) {
          try {
              Thread.sleep(1000);
          } catch (InterruptedException ex) {
              ex.printStackTrace();
          }
          this.value = value;
      }

      // Initialisation à la volée
      public static Singleton getInstance(String value) {
          if (instance == null) {
              instance = new Singleton(value);
          }
          return instance;
      }
  }
  ```

  ```java
  public class DemoSingleThread {
      public static void main(String[] args) {
          System.out.println("If you see the same value, then singleton was reused (yay!)" + "\n" +
                  "If you see different values, then 2 singletons were created (booo!!)" + "\n\n" +
                  "RESULT:" + "\n");
          Singleton singleton = Singleton.getInstance("FOO");
          Singleton anotherSingleton = Singleton.getInstance("BAR");
          System.out.println(singleton.value);
          System.out.println(anotherSingleton.value);
      }
  }
  ```

  => Dans le cas présent, le même singleton est réutilisé dans les deux cas, donc on voit la même valeur "FOO" affichée deux fois.

  ```
  If you see the same value, then singleton was reused (yay!)
  If you see different values, then 2 singletons were created (booo!!)

  RESULT:

  FOO
  FOO
  ```

  = Décorateur

  === Principe

  #principia(
    [
      => Permet d'ajouter dynamiquement des fonctionnalités à un objet sans modifier sa structure.

      => Utilisé pour les flux d'entrée/sortie, les interfaces graphiques, etc.

      => Là où l'héritage est statique, le décorateur permet d'ajouter des fonctionnalités de manière dynamique.

      => On ne peut hériter que d'une seule classe, mais on peut décorer un objet avec plusieurs décorateurs.
    ],
    rounded-image(image("/assets/image-5.png"), caption: "Utilisation simple du pattern Décorateur"),
  )

  === Exemple

  ```java
  // Interface abstraite
  abstract class DataSource {
    String fetchData();
  }

  // Classe concrète
  class FileDataSource implements DataSource {
    @Override
    String fetchData() {
      return "Hello, World!"; // => Simulation d'une lecture de fichier
    }
  }

  // Décorateur de base
  class DataSourceDecorator implements DataSource {
    final DataSource _wrappee;
    DataSourceDecorator(this._wrappee);

    @Override
    String fetchData() {
      return _wrappee.fetchData(); // On ne change rien, la modification est faite dans les décorateurs concrets
    }
  }

  // Décorateur 1 : Loggeur
  class LoggingDecorator extends DataSourceDecorator {
    LoggingDecorator(DataSource wrappee) : super(wrappee);

    @Override
    String fetchData() {
      print("[Logging] About to fetch data...");
      String data = super.fetchData();
      print("[Logging] Fetched data: $data");
      return data;
    }
  }

  // Décorateur 2 : Transformateur de données
  class UppercaseDecorator extends DataSourceDecorator {
    UppercaseDecorator(DataSource wrappee) : super(wrappee);

    @Override-
    String fetchData() {
      String data = super.fetchData();
      return data.toUpperCase(); // met en majuscules
    }
  }
  ```
]
