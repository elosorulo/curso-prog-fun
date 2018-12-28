case class User(name: String)

case class Email(
  subject: String,
  text: String,
  sender: String,
  recipient: String)

type EmailFilter = Email => Boolean

trait EmailRepository {
  def getMails(user: User, unread: Boolean): Seq[Email]
}
trait FilterRepository {
  def getEmailFilter(user: User): EmailFilter
}
trait MailboxService {
  def getNewMails(emailRepo: EmailRepository)(filterRepo: FilterRepository)(user: User) =
    emailRepo.getMails(user, true).filter(filterRepo.getEmailFilter(user))
  val newMails: User => Seq[Email]
}

object MockEmailRepository extends EmailRepository {
  def getMails(user: User, unread: Boolean): Seq[Email] = Nil
}
object MockFilterRepository extends FilterRepository {
  def getEmailFilter(user: User): EmailFilter = _ => true
}
object MailboxServiceWithMockDeps extends MailboxService {
  val newMails: (User) => Seq[Email] =
    getNewMails(MockEmailRepository)(MockFilterRepository) _
}
