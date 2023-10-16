package foo.bar.clean.domain.services.csv

const val CSV_LINE_TERMINATOR = "\r\n"
const val CSV_DEFAULT_DELIMITER = ","

class CsvBuilder(
    private val delimiter: String = CSV_DEFAULT_DELIMITER,
) {

    private val stringBuffer = StringBuffer()

    init {
        require(delimiter.isNotBlank()) {
            throw RuntimeException("delimiter needs to be at least 1 non-whitespace character")
        }
    }

    /**
     *
     * @param columns must not contain the delimiter
     */
    fun addLine(vararg columns: String): CsvBuilder {
        for (i in columns.indices) {
            require(!columns[i].contains(delimiter)) {
                throw RuntimeException("current restriction is that the columns cannot contain the delimiter [$delimiter]")
            }
            stringBuffer.append(columns[i])
            stringBuffer.append(delimiter)
        }
        if (stringBuffer.isNotEmpty()) {
            stringBuffer.setLength(stringBuffer.length - delimiter.length)
        }
        stringBuffer.append(CSV_LINE_TERMINATOR)
        return this
    }

    fun build(): String {
        return stringBuffer.toString()
    }
}
