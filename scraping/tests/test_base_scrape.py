from unittest.mock import patch, MagicMock, ANY

from main.models.errors.config_reader_error import ConfigReaderError
from main.models.errors.http_request_error import HttpRequestError
from main.models.post_collection import PostCollection


class TestBaseScraper:
    @classmethod
    def set_up_class(cls):
        pass

    @patch("main.service.base_scrape_service.Response")
    def test_send_post_collections_to_extr_with_empty_list(self, mock_response):
        response = self.scraper_service.send_post_collections_to_extraction_service([])
        mock_response.assert_called_with(status=200)
        self.assertEqual(response, mock_response.return_value)

    @patch("main.service.base_scrape_service.Response")
    @patch("main.service.base_scrape_service.make_http_request")
    def test_send_post_collections_to_extr_with_successful_request(
        self, mock_http_request, mock_response
    ):
        post_collection = MagicMock()
        response_mock = MagicMock()
        response_mock.status_code = 200
        mock_http_request.return_value = response_mock

        response = self.scraper_service.send_post_collections_to_extraction_service(
            [post_collection]
        )

        mock_response.assert_called_once_with(
            status=200,
            response=ANY,
        )
        self.assertEqual(response.status_code, mock_response.return_value.status_code)

    @patch("main.service.base_scrape_service.Response")
    @patch("main.service.base_scrape_service.make_http_request")
    def test_send_post_collections_to_extr_with_http_error(
        self, mock_http_request, mock_response
    ):
        post_collection = MagicMock()
        response_mock = MagicMock()
        response_mock.status_code = 500
        mock_http_request.return_value = response_mock

        mock_http_request.side_effect = HttpRequestError(
            "Http Request Error", response_mock
        )

        response = self.scraper_service.send_post_collections_to_extraction_service(
            [post_collection]
        )

        mock_response.assert_called_once_with(
            status=500,
            response=ANY,
        )
        self.assertEqual(response.status_code, mock_response.return_value.status_code)

    @patch("main.service.base_scrape_service.Response")
    def test_send_post_collections_to_ext_with_config_error(self, mock_response):
        post_collection = MagicMock()

        with patch.object(
            self.scraper_service.config_reader, "read_config"
        ) as mock_read_config:
            mock_read_config.side_effect = ConfigReaderError("Config Error")

            response = self.scraper_service.send_post_collections_to_extraction_service(
                [post_collection]
            )

        mock_response.assert_called_once_with(
            status=500,
            response=ANY,
        )
        self.assertEqual(response.status_code, mock_response.return_value.status_code)

    def test_transform_dto_returns_correct_post_collection(self):
        trending_statuses = [self.trend_status]
        post_collection = self.scraper_service.transform_status_dtos_to_post_collection(
            source=self.correct_source,
            statuses=trending_statuses,
            mapping_function=self.mapping_function,
        )
        self.assertIsInstance(post_collection, PostCollection)

        self.assertNotEqual(post_collection.posts, [])
        self.assertNotEqual(post_collection.trigger_timestamp, None)
        self.assertNotEqual(post_collection.created_at, None)
        self.assertNotEqual(post_collection.source, None)

        self.assertEqual(post_collection.source.source_type, self.expected_source_type)
        self.assertEqual(post_collection.posts[0].post_id, self.trend_status["id"])

    def test_transform_dto_with_empty_trend_status_returns_no_posts_in_post_collection(
        self,
    ):
        empty_trending_statuses = []
        post_collection = self.scraper_service.transform_status_dtos_to_post_collection(
            source=self.correct_source,
            statuses=empty_trending_statuses,
            mapping_function=self.mapping_function,
        )
        self.assertIsInstance(post_collection, PostCollection)
        self.assertEqual(post_collection.posts, [])

    def test_check_instance_details_with_incorrect_details_returns_false(self):
        instance_details = self.scraper_service.check_required_keys_in_json_object(
            self.incorrect_test_instance,
            self.required_instance_details,
        )
        self.assertFalse(instance_details)

    def test_check_instance_details_with_correct_details_returns_true(self):
        instance_details = self.scraper_service.check_required_keys_in_json_object(
            self.correct_test_instance,
            self.required_instance_details,
        )
        self.assertTrue(instance_details)
