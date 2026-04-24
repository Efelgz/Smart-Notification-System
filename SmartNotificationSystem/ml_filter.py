from __future__ import annotations

import re
from typing import Dict, Iterable, List, Tuple


class MLFilter:
    def __init__(self, initial_keywords: Iterable[str] | None = None, threshold: float = 1.0):
        default_keywords = ["acil", "toplanti", "hata", "fatura", "deadline", "sunum"]
        base_keywords = list(initial_keywords) if initial_keywords is not None else default_keywords

        self.__keyword_weights: Dict[str, float] = {
            keyword.strip().lower(): 1.0
            for keyword in base_keywords
            if keyword.strip()
        }
        self.__threshold = float(threshold)

    def set_keyword_weights(self, weights: Dict[str, float]) -> None:
        cleaned = {
            keyword.strip().lower(): float(weight)
            for keyword, weight in weights.items()
            if keyword and keyword.strip()
        }
        if cleaned:
            self.__keyword_weights = cleaned

    def get_keyword_weights(self) -> Dict[str, float]:
        return dict(self.__keyword_weights)

    def top_keywords(self, limit: int = 10) -> List[Dict[str, float]]:
        ranked = sorted(
            self.__keyword_weights.items(),
            key=lambda item: item[1],
            reverse=True,
        )
        return [
            {"keyword": keyword, "weight": round(weight, 3)}
            for keyword, weight in ranked[: max(1, limit)]
        ]

    def analyze_content(self, text: str) -> bool:
        is_critical, _, _ = self.analyze_content_details(text)
        return is_critical

    def analyze_content_details(self, text: str) -> Tuple[bool, float, List[str]]:
        text_lower = text.lower()
        score = 0.0
        matched_keywords: List[str] = []

        for keyword, weight in self.__keyword_weights.items():
            if keyword in text_lower:
                score += weight
                matched_keywords.append(keyword)

        is_critical = score >= self.__threshold
        return is_critical, round(score, 3), matched_keywords

    def update_model(self, text: str, is_important: bool, learning_rate: float = 0.2) -> List[str]:
        tokens = self.__extract_tokens(text)
        changed_keywords: List[str] = []

        for token in tokens:
            if len(token) < 3:
                continue

            old_weight = self.__keyword_weights.get(token, 0.1)
            if is_important:
                new_weight = old_weight + learning_rate
            else:
                new_weight = max(0.0, old_weight - learning_rate)

            self.__keyword_weights[token] = round(new_weight, 3)
            changed_keywords.append(token)

        return changed_keywords

    @staticmethod
    def __extract_tokens(text: str) -> List[str]:
        return list(dict.fromkeys(re.findall(r"\b[\w']+\b", text.lower())))