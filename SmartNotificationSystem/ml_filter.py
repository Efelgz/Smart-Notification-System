class MLFilter:
    def __init__(self):
        self.__critical_keywords = ["acil", "toplantı", "hata", "fatura", "deadline", "sunum"]

    def analyze_content(self, text: str) -> bool:
        text_lower = text.lower()
        for keyword in self.__critical_keywords:
            if keyword in text_lower:
                return True
        return False