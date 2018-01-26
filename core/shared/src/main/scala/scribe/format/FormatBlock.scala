package scribe.format

import java.text.SimpleDateFormat

import scribe.LogRecord

trait FormatBlock {
  def format(record: LogRecord, b: java.lang.StringBuilder): Unit
}

object FormatBlock {
  case class RawString(s: String) extends FormatBlock {
    override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = b.append(s)
  }

  object Date {
    object Standard extends FormatBlock {
      private lazy val sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z")

      override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = {
        val l = record.timeStamp
        // TODO: find a faster f"" interpolator option
        b.append(sdf.format(l))
      }
    }
  }

  object ThreadName extends FormatBlock {
    override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = b.append(record.thread.getName)
  }

  object Level {
    object PaddedRight extends FormatBlock {
      override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = b.append(record.level.namePaddedRight)
    }
  }

  object Position {
    object Abbreviated extends FormatBlock {
      override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = {
        val parts = record.className.split('.')
        val last = parts.length - 1
        val abbreviation = parts.zipWithIndex.map {
          case (cur, i) if i == last => cur
          case (cur, _) => cur.head
        }.mkString(".")
        b.append(abbreviation)
      }
    }
  }

  object Message extends FormatBlock {
    override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = b.append(record.message)
  }

  object NewLine extends FormatBlock {
    override def format(record: LogRecord, b: java.lang.StringBuilder): Unit = b.append('\n')
  }
}