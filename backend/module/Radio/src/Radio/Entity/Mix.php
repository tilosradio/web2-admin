<?php
namespace Radio\Entity;

use Doctrine\ORM\Mapping as ORM,
    Radio\Permissions\Acl,
    Radio\Entity\Role;

/**
 * @ORM\Entity
 * @ORM\Table(name="mix")
 * */
class Mix
{
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue
     * */
    private $id;

    /**
     * @ORM\Column(type="string",length=160)
     */
    private $author;

    /**
     * @ORM\Column(type="string",length=160, nullable=false)
     */
    private $title;

    /**
     * @ORM\Column(type="string",length=255, nullable=false)
     */
    private $file;

    /**
     * @ORM\ManyToOne(targetEntity="Show", fetch="EAGER")
     */
    protected $show;

    /**
     * @ORM\Column(type="date", nullable=true)
     * */
    protected $date;

    /**
     * @ORM\Column(type="integer")
     * */
    protected $type = 0;

    /**
         * @ORM\Column(type="integer")
         * */
        protected $category = 0;

    /**
     * @return mixed
     */
    public function getAuthor()
    {
        return $this->author;
    }

    /**
     * @param mixed $author
     */
    public function setAuthor($author)
    {
        $this->author = $author;
    }

    /**
     * @return mixed
     */
    public function getDate()
    {
        return $this->date;
    }

    /**
     * @param mixed $date
     */
    public function setDate($date)
    {
        $this->date = $date;
    }

    /**
     * @return mixed
     */
    public function getFile()
    {
        return $this->file;
    }

    /**
     * @param mixed $file
     */
    public function setFile($file)
    {
        $this->file = $file;
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     */
    public function setId($id)
    {
        $this->id = $id;
    }

    /**
     * @return mixed
     */
    public function getShow()
    {
        return $this->show;
    }

    /**
     * @param mixed $show
     */
    public function setShow($show)
    {
        $this->show = $show;
    }

    /**
     * @return mixed
     */
    public function getTitle()
    {
        return $this->title;
    }

    /**
     * @param mixed $title
     */
    public function setTitle($title)
    {
        $this->title = $title;
    }

    /**
     * @return mixed
     */
    public function getType()
    {
        return $this->type;
    }

    /**
     * @param mixed $type
     */
    public function setType($type)
    {
        $this->type = $type;
    }


}