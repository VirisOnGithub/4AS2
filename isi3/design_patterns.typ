#import "../template/polytech.typ": *;

#show: conf(doctitle: "Design Patterns", subject: "ISI3", theme: rgb("#079452"))[
  #titlepage(
    authors: "Clément RENIERS",
  )

  = Stratégie

  === Principe

  #grid(
    columns: (1fr, 1fr),
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

  #grid(
    columns: (1fr, 1fr),
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

  #grid(
    columns: (1fr, 1fr),
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
]
