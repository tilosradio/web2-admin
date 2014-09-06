
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue
     * */
    protected $id;
    /**
     * @ORM\Column(type="datetime")
     * */
    protected $start;
    /**
     * @ORM\Column(type="datetime")
     * */
    protected $end;
    /**
     * @ORM\ManyToOne(targetEntity = "Show")
     * @ORM\JoinColumn(name = "radioshow_id", referencedColumnName = "id")
     */
    protected $show;
    /**
     * @ORM\ManyToOne(targetEntity = "Episode")
     * @ORM\JoinColumn(name = "episode_id", referencedColumnName = "id")
     */
    protected $episode;

    /**
     * @ORM\ManyToOne(targetEntity = "User")
     * @ORM\JoinColumn(name = "user_id", referencedColumnName = "id")
     */
    protected $author;
    /**
     * @ORM\Column(type="datetime")
     * */
    protected $modified;
    /**
     * @ORM\Column(type="string",length=160)
     * */
    protected $title;
    /**
     * @ORM\Column(type="text")
     * */
    protected $content;